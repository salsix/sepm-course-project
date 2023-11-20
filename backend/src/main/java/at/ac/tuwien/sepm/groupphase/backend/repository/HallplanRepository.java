package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallplanRepository extends JpaRepository<Hallplan, Long> {

    @Query("SELECT h FROM Hallplan h " +
        "LEFT JOIN FETCH h.location " +
        "LEFT JOIN FETCH h.cats " +
        "LEFT JOIN FETCH h.seats " +
        "LEFT JOIN FETCH h.areas WHERE h.id=:id")
    Optional<Hallplan> findByIdEager(@Param("id") Long id);

    @Query("SELECT h FROM Hallplan h " +
        "LEFT JOIN FETCH h.location WHERE h.id=:id")
    Optional<Hallplan> findByIdEagerOnlyLocation(@Param("id") Long id);

    @Query("SELECT h FROM Hallplan h " +
        "LEFT JOIN FETCH h.cats " +
        "LEFT JOIN FETCH h.seats " +
        "LEFT JOIN FETCH h.areas " +
        "WHERE h.id=:id")
    Optional<Hallplan> findByIdEagerNoLocation(@Param("id") Long id);
}
