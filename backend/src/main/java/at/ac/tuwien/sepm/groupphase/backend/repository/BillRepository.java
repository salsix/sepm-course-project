package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT b FROM Bill b " +
        "LEFT JOIN FETCH b.show s LEFT JOIN FETCH b.seats LEFT JOIN FETCH b.areas " +
        "LEFT JOIN FETCH s.event " +
        "LEFT JOIN FETCH s.hallplan h " +
        "LEFT JOIN FETCH h.location " +
        "LEFT JOIN FETCH h.cats " +
        "LEFT JOIN FETCH h.seats " +
        "LEFT JOIN FETCH h.areas " +
        "WHERE b.id=:id")
    Optional<Bill> findByIdEager(@Param("id") Long id);


    @Query("SELECT b FROM Bill b " +
        "LEFT JOIN FETCH b.show s " +
        "LEFT JOIN FETCH s.event LEFT JOIN FETCH b.seats LEFT JOIN FETCH b.areas " +
        "WHERE b.applicationUser=:user")
    List<Bill> findAllByApplicationUserEager(@Param("user") ApplicationUser user);

    @Query("SELECT b FROM Bill b " +
        "LEFT JOIN FETCH b.show s " +
        "LEFT JOIN FETCH s.event " +
        "WHERE b.storno=FALSE AND b.applicationUser=:user AND " +
        "(b.show.date>:date OR " +
            "(b.show.date=:date AND (b.show.hour>:hour OR " +
                "(b.show.hour=:hour AND b.show.minute>=:minute)" +
            "))" +
        ")")
    List<Bill> findActiveByFutureOrPresentAndApplicationUserEager(@Param("user") ApplicationUser user,
                                                                  @Param("date") LocalDate date,
                                                                  @Param("hour") Integer hour,
                                                                  @Param("minute") Integer minute);

    @Query("SELECT b FROM Bill b " +
        "LEFT JOIN FETCH b.show s " +
        "LEFT JOIN FETCH s.event e " +
        "WHERE b.applicationUser=:user AND b.storno=TRUE OR (" +
        "(b.show.date<:date OR " +
            "(b.show.date=:date AND (b.show.hour<:hour OR " +
                "(b.show.hour=:hour AND b.show.minute<=:minute)" +
            "))" +
        ")) ORDER BY s.date DESC, s.hour DESC, s.minute DESC")
    List<Bill> findByPastAndApplicationUserEager(@Param("user") ApplicationUser user,
                                                 @Param("date") LocalDate date,
                                                 @Param("hour") Integer hour,
                                                 @Param("minute") Integer minute, Pageable page);

    @Query("SELECT e,SUM(b.ticketCount) FROM Bill b LEFT JOIN b.show s LEFT JOIN s.event e " +
        "WHERE e.category.category=:category AND b.buyTime>:date " +
        "GROUP BY e " +
        "ORDER BY SUM(b.ticketCount) DESC,e.name ASC,e.duration DESC")
    List<Tuple> topTen(@Param("category") String category, @Param("date") LocalDateTime date, Pageable page);
}
