package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l LEFT JOIN FETCH l.hallplans WHERE l.id=:id")
    Optional<Location> findByIdWithHallplans(@Param("id") Long id);

    @Query("SELECT DISTINCT l.country FROM Location l ORDER BY l.country")
    List<String> getCountries();

    @Query("SELECT DISTINCT l.zip FROM Location l WHERE l.country=:country ORDER BY l.zip")
    List<String> getCountryZips(@Param("country") String country);

    @Query("SELECT l FROM Location l WHERE l.country=:country AND l.zip=:zip ORDER BY l.name")
    List<Location> getCountryZipLocations(@Param("country") String country, @Param("zip") String zip);
}
