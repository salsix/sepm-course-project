package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAll() {
        LOGGER.trace("getAll()");
        return locationRepository.findAll();
    }

    @Override
    @Transactional
    public Location getById(Long id) {
        LOGGER.trace("getById({})", id);
        return locationRepository.findByIdWithHallplans(id)
            .orElseThrow(() -> new NotFoundException("Location does not exist"));
    }

    @Override
    public List<String> getCountries() {
        return locationRepository.getCountries();
    }

    @Override
    public List<String> getCountryZips(String country) {
        return locationRepository.getCountryZips(country);
    }

    @Override
    public List<Location> getCountryZipLocations(String country, String zip) {
        return locationRepository.getCountryZipLocations(country, zip);
    }

    @Override
    public Location save(Location location) {
        LOGGER.trace("save({})", location);
        location.setId(null);
        return locationRepository.save(location);
    }

    @Override
    @Transactional
    public void edit(Location location) {
        LOGGER.trace("edit({})", location);
        if (location.getId() == null) {
            throw new ValidationException("cannot update Location without id");
        }
        locationRepository.findById(location.getId())
            .orElseThrow(() -> new NotFoundException("Location does not exist"));
        locationRepository.save(location);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LOGGER.trace("delete({})", id);

        //check if exists
        Location location = locationRepository.findByIdWithHallplans(id)
            .orElseThrow(() -> new NotFoundException("Location does not exist"));

        //TODO: check not used in Shows

        //delete
        locationRepository.delete(location);
    }
}
