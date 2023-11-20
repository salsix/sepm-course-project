package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import com.sun.istack.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("SELECT s FROM Show s " +
        "LEFT JOIN FETCH s.event " +
        "LEFT JOIN FETCH s.hallplan h " +

        "LEFT JOIN FETCH h.location " +

        "WHERE s.id=:id")
    Optional<Show> findByIdEagerHallplanLocation(@Param("id") Long id);

    @Query("SELECT s FROM Show s " +
        "LEFT JOIN FETCH s.event " +
        "LEFT JOIN FETCH s.hallplan h " +

        "LEFT JOIN FETCH h.location " +
        "LEFT JOIN FETCH h.cats " +
        "LEFT JOIN FETCH h.seats " +
        "LEFT JOIN FETCH h.areas " +

        "WHERE s.id=:id")
    Optional<Show> findByIdEagerHallplan(@Param("id") Long id);

    List<Show> findAll(@Nullable Specification<Show> showSpec, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Show s WHERE s.hallplan=:hallplan")
    Integer findByHallplan(@Param("hallplan") Hallplan hallplan);
}
