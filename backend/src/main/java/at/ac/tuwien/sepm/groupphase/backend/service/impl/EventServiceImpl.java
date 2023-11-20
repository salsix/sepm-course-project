package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final ImageRepository imageRepository;
    private final EventCategoryRepository eventCategoryRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ImageRepository imageRepository, EventCategoryRepository eventCategoryRepository) {
        this.eventRepository = eventRepository;
        this.imageRepository = imageRepository;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findEvents(Specification<Event> eventSpec, int page) {

        Pageable pageable = PageRequest.of(page, 5);

        return eventRepository.findAll(eventSpec, pageable);
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.findByIdEager(id)
            .orElseThrow(() -> new NotFoundException("Event does not exist"));
    }

    @Override
    public Event save(Event event) {
        event.setId(null);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public void edit(Event event) {
        LOGGER.trace("edit {}", event);
        if (event.getId() == null) {
            throw new ValidationException("cannot update Event without id");
        }
        eventRepository.findByIdEager(event.getId())
            .orElseThrow(() -> new NotFoundException("Event does not exist"));
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void saveImage(Long eventId, MultipartFile image) {
        LOGGER.trace("saveImage({},{})", eventId, image.getOriginalFilename());

        //check type
        String type = image.getContentType();
        if (type == null) {
            throw new ValidationException("Could not read image");
        }

        boolean matchingType = false;
        for (String imageType : IMAGE_TYPES) {
            if (type.equals(imageType)) {
                matchingType = true;
                break;
            }
        }
        if (!matchingType) {
            throw new ValidationException("Only .png and .jpeg allowed for image upload");
        }

        //check event exists
        Event event = eventRepository.findByIdEager(eventId)
            .orElseThrow(() -> new NotFoundException("Event does not exist"));

        //save image
        byte[] bytes;
        try {
            bytes = image.getBytes();
        } catch (IOException e) {
            //TODO: maybe another type of exception for this
            throw new ValidationException("Could not process image");
        }
        imageRepository.save("event-" + event.getId(), bytes);

        //update image in event
        event.setImage(true);
        eventRepository.save(event);
    }

    @Override
    public byte[] getImage(Long eventId) {
        LOGGER.trace("getImage({})", eventId);
        return imageRepository.get("event-" + eventId);
    }

    @Override
    @Transactional
    public void saveCategory(String category) {
        if (eventCategoryRepository.findById(category).isPresent()) {
            throw new ConflictException("category exists already");
        }
        eventCategoryRepository.save(new EventCategory(category));
    }

    @Override
    public List<String> getCategories() {
        List<EventCategory> list = eventCategoryRepository.findAll();
        ArrayList<String> stringList = new ArrayList<>();
        for (EventCategory e : list) {
            stringList.add(e.getCategory());
        }
        return stringList;
    }
}
