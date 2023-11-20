package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(LocationEndpoint.BASE_URL)
public class LocationEndpoint {

    static final String BASE_URL = "/api/v1/locations";
    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationEndpoint(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "")
    @Operation(summary = "get a location with all infos with given id")
    public List<LocationSimpleDto> getAll() {
        LOGGER.info("GET " + BASE_URL);
        return locationMapper.locationEntityToSimpleDto(locationService.getAll());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "get a location with all infos with given id")
    public LocationDto getById(@NotNull @PathVariable("id") Long id) {
        LOGGER.info("GET " + BASE_URL + "/{}", id);
        return locationMapper.locationEntityToDto(locationService.getById(id));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/countries")
    @Operation(summary = "get a list of countries, where there are locations")
    public List<String> getCountries() {
        LOGGER.info("GET " + BASE_URL + "/countries");
        return locationService.getCountries();
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/countries/{country}/zips")
    @Operation(summary = "get a list of countries, where there are locations")
    public List<String> getCountryZips(@PathVariable("country") String country) {
        LOGGER.info("GET " + BASE_URL + "/countries/" + country + "/zips");
        return locationService.getCountryZips(country);
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/countries/{country}/zips/{zip}/locations")
    @Operation(summary = "get a list of countries, where there are locations")
    public List<LocationSimpleDto> getCountryZipLocations(@PathVariable("country") String country, @PathVariable("zip") String zip) {
        LOGGER.info("GET " + BASE_URL + "/countries/" + country + "/zips/" + zip + "/locations");
        return locationMapper.locationEntityToSimpleDto(locationService.getCountryZipLocations(country, zip));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "")
    @Operation(summary = "save a new location")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto save(@Valid @RequestBody LocationDto locationDto) {
        LOGGER.info("POST " + BASE_URL + " new: {}", locationDto);
        return locationMapper.locationEntityToDto(
            locationService.save(locationMapper.locationDtoToEntity(locationDto)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "")
    @Operation(summary = "update location")
    public void update(@Valid @RequestBody LocationDto locationDto) {
        LOGGER.info("PUT " + BASE_URL + " edit: {}", locationDto);
        locationService.edit(locationMapper.locationDtoToEntity(locationDto));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "delete location")
    public void delete(@NotNull @PathVariable("id") Long id) {
        LOGGER.info("DELETE " + BASE_URL + "/{}", id);
        locationService.delete(id);
    }
}
