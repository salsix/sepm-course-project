package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    @Query("SELECT b FROM BookedSeat b WHERE b.show.id=:id AND b.storno=false")
    List<BookedSeat> findActiveByShowId(@Param("id") Long id);

    @Query("SELECT b FROM BookedSeat b WHERE b.show.id=:id AND b.storno=false AND b.identifier IN (:list)")
    List<BookedSeat> findActiveByShowIdAndIdentifier(@Param("id") Long id,
                                                     @Param("list") Set<String> list);
}
