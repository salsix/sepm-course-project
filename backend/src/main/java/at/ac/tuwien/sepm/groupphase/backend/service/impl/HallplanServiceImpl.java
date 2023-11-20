package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanArea;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanSeat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidHallplanExeption;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallplanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallplanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service
public class HallplanServiceImpl implements HallplanService {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());

    private final HallplanRepository hallplanRepository;
    private final LocationRepository locationRepository;
    private final ShowRepository showRepository;

    @Autowired
    public HallplanServiceImpl(HallplanRepository hallplanRepository,
                               LocationRepository locationRepository,
                               ShowRepository showRepository) {
        this.hallplanRepository = hallplanRepository;
        this.locationRepository = locationRepository;
        this.showRepository = showRepository;
    }

    private void validateHallplan(Hallplan hallplan) {
        int cats = hallplan.getCats().size();
        HashMap<String, ArrayList<Integer>> seatMap = new HashMap<>();
        HashMap<String, Integer> seatCats = new HashMap<>();
        Set<HallplanSeat> seats = hallplan.getSeats();
        for (HallplanSeat s : seats) {
            String id = s.getIdentifier() + s.getRowNumber();

            //check cat exists - should only happen with malicious attacks
            if (s.getCat() >= cats) {
                throw new ValidHallplanExeption("Hallplan seats cats invalid (row " + id + ")");
            }

            //id not yet used
            if (!seatMap.containsKey(id)) {
                seatMap.put(id, new ArrayList<>());
                seatCats.put(id, s.getCat());
            } else {
                //id already seen

                //check if not unique seats
                ArrayList<Integer> list = seatMap.get(id);
                if (list.contains(s.getStartNumber()) || list
                    .contains(s.getStartNumber() + s.getCount() - 1)) {
                    throw new ValidHallplanExeption("Hallplan seats are not unique (row " + id + ")");
                }

                //check if same row same cat
                if (!seatCats.get(id).equals(s.getCat())) {
                    throw new ValidHallplanExeption("Hallplan single seat row must have same price (row " + id + ")");
                }
            }

            //add all seats to list
            ArrayList<Integer> list = seatMap.get(id);
            for (int i = 0; i < s.getCount(); ++i) {
                list.add(s.getStartNumber() + i);
            }
        }

        //check area
        ArrayList<String> areaList = new ArrayList<>();
        Set<HallplanArea> areas = hallplan.getAreas();
        for (HallplanArea a : areas) {
            String id = a.getIdentifier() + a.getRowNumber();

            //check cat exists - should only happen with malicious attacks
            if (a.getCat() >= cats) {
                throw new ValidHallplanExeption("Hallplan area cats invalid (row " + id + ")");
            }

            //check unique
            if (areaList.contains(id)) {
                throw new ValidHallplanExeption("Hallplan areas are not unique (" + id + ")");
            }
            areaList.add(id);
        }
    }

    @Override
    public Hallplan getById(Long id) {
        LOGGER.trace("getById {}", id);
        return hallplanRepository.findByIdEager(id)
            .orElseThrow(() -> new NotFoundException("Hallplan does not exist"));
    }

    @Override
    @Transactional
    public Hallplan save(Long id, Hallplan hallplan) {
        LOGGER.trace("save {}: {}", id, hallplan);
        validateHallplan(hallplan);

        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Location does not exist"));
        hallplan.setLocation(location);

        hallplan.setId(null);
        return hallplanRepository.save(hallplan);
    }

    @Override
    @Transactional
    public Hallplan edit(Hallplan hallplan) {
        LOGGER.trace("edit {}", hallplan);
        if (hallplan.getId() == null) {
            throw new ValidHallplanExeption("cannot update Hallplan without id");
        }

        //check not used by Shows
        Integer count = showRepository.findByHallplan(hallplan);
        if (count > 0) {
            throw new ConflictException("Cannot delete hallplan - hallplan is used by " + count + " shows.");
        }

        validateHallplan(hallplan);

        Hallplan saved = hallplanRepository.findByIdEager(hallplan.getId())
            .orElseThrow(() -> new NotFoundException("Hallplan does not exist"));
        hallplan.setLocation(saved.getLocation());

        return hallplanRepository.save(hallplan);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        //get
        Hallplan hallplan = hallplanRepository.findByIdEager(id)
            .orElseThrow(() -> new NotFoundException("Hallplan does not exist"));

        //check not used by Shows
        Integer count = showRepository.findByHallplan(hallplan);
        if (count > 0) {
            throw new ConflictException("Cannot delete hallplan - hallplan is used by " + count + " shows.");
        }

        //delete
        hallplanRepository.delete(hallplan);
    }
}
