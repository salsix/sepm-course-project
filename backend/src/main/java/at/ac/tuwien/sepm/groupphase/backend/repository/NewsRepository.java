package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Lists up all news that a user has already read.
     *
     * @param userId id of the user
     * @return list of all news that are already read of the user
     */
    @Query("select n from News n inner join n.wasReadByUsers wRBU where wRBU.id in :userId")
    List<News> getPreviousNews(@Param("userId") Long userId);

    /**
     * Lists up all news that a user has not already read.
     *
     * @param userId id of the user
     * @return list of all news that the user is able to read and are not flagged
     */
    @Query("select n from News n WHERE n.id NOT in (SELECT n.id from n.wasReadByUsers wRBU where wRBU.id = :userId)")
    List<News> getCurrentNews(@Param("userId") Long userId);
}
