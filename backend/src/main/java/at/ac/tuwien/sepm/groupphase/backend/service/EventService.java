package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    String[] IMAGE_TYPES = {"image/png", "image/jpeg"};

    /**
     * gets all events.
     *
     * @return all events
     */
    List<Event> getAll();

    /**
     * search for events.
     *
     * @param eventSpec search specification
     * @param page pagination number
     * @return list of found events
     */
    List<Event> findEvents(Specification<Event> eventSpec, int page);

    /**
     * get event by id.
     *
     * @param id event id
     * @return event
     */
    Event getById(Long id);

    /**
     * save new event.
     *
     * @param event event to save
     * @return event with saved id
     */
    Event save(Event event);

    /**
     * edit event.
     *
     * @param event event to edit
     */
    void edit(Event event);

    /**
     * save image to event.
     *
     * @param eventId event id
     * @param image image to save
     */
    void saveImage(Long eventId, MultipartFile image);

    /**
     * get event image.
     *
     * @param eventId event id
     * @return image as bytes
     */
    byte[] getImage(Long eventId);

    /**
     * save new event category.
     *
     * @param category category name
     */
    void saveCategory(String category);

    /**
     * get all event categories.
     *
     * @return all event categories
     */
    List<String> getCategories();
}
