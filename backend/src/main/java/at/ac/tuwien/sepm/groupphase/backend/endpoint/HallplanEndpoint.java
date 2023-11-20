package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.HallplanMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.HallplanService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(HallplanEndpoint.BASE_URL)
public class HallplanEndpoint {

    static final String BASE_URL = "/api/v1/hallplans";
    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private final HallplanService hallplanService;
    private final HallplanMapper hallplanMapper;

    @Autowired
    public HallplanEndpoint(HallplanService hallplanService, HallplanMapper hallplanMapper) {
        this.hallplanService = hallplanService;
        this.hallplanMapper = hallplanMapper;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "get a hallplan with all infos with given id")
    public HallplanDto getById(@NotNull @PathVariable("id") Long id) {
        LOGGER.info("GET " + BASE_URL + "/{}", id);
        return hallplanMapper.hallplanEntityToDto(hallplanService.getById(id));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/{locationId}")
    @Operation(summary = "save a new hallplan belonging to location with locationId")
    @ResponseStatus(HttpStatus.CREATED)
    public HallplanDto save(@NotNull @PathVariable("locationId") Long locationId,
                            @Valid @RequestBody HallplanDto hallplanDto) {
        LOGGER.info("POST " + BASE_URL + " new: {}: {}", locationId, hallplanDto);
        return hallplanMapper.hallplanEntityToDto(
            hallplanService.save(locationId, hallplanMapper.hallplanDtoToEntity(hallplanDto)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "")
    @Operation(summary = "update hallplan")
    @ValidateOnExecution
    public HallplanDto edit(@Valid @RequestBody HallplanDto hallplanDto) {
        LOGGER.info("PUT " + BASE_URL + " edit: {}", hallplanDto);
        return hallplanMapper.hallplanEntityToDto(
            hallplanService.edit(hallplanMapper.hallplanDtoToEntity(hallplanDto)));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "delete hallplan")
    public void delete(@NotNull @PathVariable("id") Long id) {
        LOGGER.info("DELETE " + BASE_URL + "/{}", id);
        hallplanService.delete(id);
    }
}
