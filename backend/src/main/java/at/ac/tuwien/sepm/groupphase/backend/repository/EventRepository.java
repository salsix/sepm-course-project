package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import com.sun.istack.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
        "LEFT JOIN FETCH e.shows s " +
        "LEFT JOIN FETCH s.event " +
        "LEFT JOIN FETCH s.hallplan h " +

        "LEFT JOIN FETCH h.location " +
        "LEFT JOIN FETCH h.cats " +
        "LEFT JOIN FETCH h.areas " +
        "LEFT JOIN FETCH h.seats " +

        "WHERE e.id=:id")
    Optional<Event> findByIdEager(@Param("id") Long id);

    List<Event> findAll(@Nullable Specification<Event> eventSpecification, Pageable pageable);

}
