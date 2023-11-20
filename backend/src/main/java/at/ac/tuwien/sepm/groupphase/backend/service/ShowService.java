package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booked;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ShowService {

    /**
     * Returns all saved shows.
     *
     * @return all saved shows
     */
    List<Show> getAll();

    /**
     * Returns list of shows according to search specifications.
     *
     * @param showSpec search specifications
     * @param page page for pagination
     * @return list of shows
     */
    List<Show> findShows(Specification<Show> showSpec, int page);

    /**
     * Returns show according to id.
     *
     * @param id id of show
     * @return show according to id
     */
    Show getById(Long id);

    /**
     * Returns saved show.
     *
     * @param eventId id of show
     * @param show show
     * @return saved show
     */
    Show save(Long eventId, Show show);

    /**
     * edits show data.
     *
     * @param show show
     */
    void edit(Show show);
}
