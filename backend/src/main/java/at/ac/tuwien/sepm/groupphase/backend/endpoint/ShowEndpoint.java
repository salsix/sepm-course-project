package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookedDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BookedMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.specification.EventSpecification;
import at.ac.tuwien.sepm.groupphase.backend.entity.specification.ShowSpecification;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = ShowEndpoint.BASE_URL)
public class ShowEndpoint {
    static final String BASE_URL = "api/v1/shows";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShowMapper showMapper;
    private final ShowService showService;
    private final TicketService ticketService;
    private final BookedMapper bookedMapper;

    @Autowired
    public ShowEndpoint(ShowMapper showMapper, ShowService showService, TicketService ticketService, BookedMapper bookedMapper) {
        this.showMapper = showMapper;
        this.showService = showService;
        this.ticketService = ticketService;
        this.bookedMapper = bookedMapper;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "gets show by id")
    ShowDto getById(@PathVariable("id") Long id) {
        LOGGER.info("GET " + BASE_URL + "/{}", id);
        return showMapper.showEntityToDto(showService.getById(id));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}/booked")
    @Operation(summary = "gets booked seats and areas from show by id")
    BookedDto getBooked(@PathVariable("id") Long id) {
        LOGGER.info("GET " + BASE_URL + "/{}/booked", id);
        return bookedMapper.entityToDto(ticketService.getAllBooked(id));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/{eventId}")
    @Operation(summary = "save new show")
    @ResponseStatus(value = HttpStatus.CREATED)
    ShowDto save(@PathVariable("eventId") Long eventId, @Valid @RequestBody ShowDto showDto) {
        LOGGER.info("POST " + BASE_URL + " new {} {}", eventId, showDto);
        return showMapper.showEntityToDto(showService.save(eventId, showMapper.showDtoToEntity(showDto)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "")
    @Operation(summary = "edit show")
    void edit(@Valid @RequestBody ShowDto showDto) {
        LOGGER.info("PUT " + BASE_URL + " edit {}", showDto);
        showService.edit(showMapper.showDtoToEntity(showDto));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/find")
    @Operation(summary = "gets all shows with certain properties")
    List<ShowDto> findShows(ShowSpecification showSpecification, int page) {
        LOGGER.info("GET " + BASE_URL + "/find page {}", page);
        return showMapper.showEntityToDto(showService.findShows(showSpecification, page));
    }
}
