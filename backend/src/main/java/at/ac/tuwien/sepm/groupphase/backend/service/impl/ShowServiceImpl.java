package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallplanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final HallplanRepository hallplanRepository;

    @Autowired
    public ShowServiceImpl(ShowRepository showRepository,
                           EventRepository eventRepository,
                           HallplanRepository hallplanRepository) {
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.hallplanRepository = hallplanRepository;
    }

    private void validateShow(Show show) {
        if (show.getHallplan() == null || show.getHallplan().getId() == null) {
            LOGGER.info("show: {}", show);
            throw new ValidationException("no Hallplan set");
        }
    }

    @Override
    public List<Show> getAll() {
        return showRepository.findAll();
    }

    @Override
    @Transactional
    public List<Show> findShows(Specification<Show> showSpec, int page) {
        PageRequest request = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "date", "hour", "minute"));
        return showRepository.findAll(showSpec, request);
    }

    @Override
    public Show getById(Long id) {
        return showRepository.findByIdEagerHallplanLocation(id).orElseThrow(() -> new NotFoundException("Show does not exist"));
    }

    @Override
    @Transactional
    public Show save(Long eventId, Show show) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event does not exist"));

        validateShow(show);

        Hallplan hallplan = hallplanRepository.findByIdEagerOnlyLocation(show.getHallplan().getId()).orElseThrow(() -> new NotFoundException("Hallplan does not exist"));

        show.setEvent(event);
        show.setId(null);
        show.setHallplan(hallplan);

        show = showRepository.save(show);
        return show;
    }

    @Override
    @Transactional
    public void edit(Show show) {
        validateShow(show);

        if (show.getId() == null) {
            throw new ValidationException("cannot update Show without id");
        }
        showRepository.findByIdEagerHallplanLocation(show.getId()).orElseThrow(() -> new NotFoundException("Show does not exist"));

        Hallplan hallplan = hallplanRepository.findById(show.getHallplan().getId()).orElseThrow(() -> new NotFoundException("Hallplan does not exist"));
        show.setHallplan(hallplan);

        showRepository.save(show);
    }
}
