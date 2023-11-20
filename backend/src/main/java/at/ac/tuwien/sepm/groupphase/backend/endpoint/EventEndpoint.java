package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopTenDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopTen;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.specification.EventSpecification;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = EventEndpoint.BASE_URL)
public class EventEndpoint {
    static final String BASE_URL = "api/v1/events";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventMapper eventMapper;
    private final EventService eventService;
    private final TicketService ticketService;

    @Autowired
    public EventEndpoint(EventMapper eventMapper, EventService eventService, TicketService ticketService) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/topTen")
    @Operation(summary = "gets top ten events this month in category")
    List<TopTenDto> topTen(@RequestParam("category") String category) {
        LOGGER.info("GET " + BASE_URL + "/topTen/{}", category);
        return eventMapper.topTenEntityToDto(ticketService.topTen(category));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "")
    @Operation(summary = "gets all events")
    List<EventSimpleDto> getAll() {
        LOGGER.info("GET " + BASE_URL + "");
        return eventMapper.eventEntityToSimpleDto(eventService.getAll());
    }

    // this will replace the getAll method once its finished
    @Secured("ROLE_USER")
    @GetMapping(value = "/find")
    @Operation(summary = "gets all events with certain properties")
    List<EventSimpleDto> findEvents(EventSpecification eventSpecification, int page) {
        LOGGER.info("GET " + BASE_URL + "");
        return eventMapper.eventEntityToSimpleDto(eventService.findEvents(eventSpecification, page));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "gets event by id")
    EventDto getById(@PathVariable("id") Long id) {
        LOGGER.info("GET " + BASE_URL + "/{}", id);
        return eventMapper.eventEntityToDto(eventService.getById(id));
    }

    @PermitAll
    @GetMapping(value = "/{eventId}/image")
    @Operation(summary = "gets event image by event id")
    Resource getImage(@PathVariable("eventId") Long eventId) {
        LOGGER.info("GET " + BASE_URL + "/{}/image", eventId);
        return new ByteArrayResource(eventService.getImage(eventId));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "")
    @Operation(summary = "save new event")
    @ResponseStatus(value = HttpStatus.CREATED)
    EventDto save(@RequestBody @Valid EventDto eventDto) {
        LOGGER.info("POST " + BASE_URL + " new {}", eventDto);
        return eventMapper.eventEntityToDto(eventService.save(eventMapper.eventDtoToEntity(eventDto)));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/{eventId}/image")
    @Operation(summary = "save new image to existing event")
    void saveImage(@PathVariable("eventId") Long eventId,
                   @RequestParam("image") MultipartFile image) {
        LOGGER.info("POST " + BASE_URL + "/{}/image image '{}' size {}", eventId, image.getOriginalFilename(), image.getSize());
        eventService.saveImage(eventId, image);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "")
    @Operation(summary = "edit event")
    void edit(@Valid @RequestBody EventDto eventDto) {
        LOGGER.info("PUT " + BASE_URL + " new {}", eventDto);
        eventService.edit(eventMapper.eventDtoToEntity(eventDto));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/categories")
    @Operation(summary = "add new category")
    void addCategory(@RequestBody String eventCategory) {
        LOGGER.info("POST " + BASE_URL + "/categories new {}", eventCategory);
        eventService.saveCategory(eventCategory);
    }

    @Secured("ROLE_USER")
    @GetMapping("/categories")
    @Operation(summary = "gets all categories")
    List<String> getCategories() {
        LOGGER.info("GET " + BASE_URL + "/categories");
        return eventService.getCategories();
    }

    //in case of delete - also delete image
}
