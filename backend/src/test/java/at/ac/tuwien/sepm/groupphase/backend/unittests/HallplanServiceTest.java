package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidHallplanExeption;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallplanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class HallplanServiceTest implements TestData {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private HallplanService hallplanService;

    @BeforeEach
    public void beforeEach() {
        locationRepository.deleteAll();
    }

    @Test
    public void test_valid() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanValid();
        hallplanService.save(location.getId(), hallplan);
    }

    @Test
    public void test_location_notFound() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanValid();
        assertThrows(NotFoundException.class,
            () -> hallplanService.save(location.getId() + 1, hallplan));
    }

    @Test
    public void test_seatsNotUnique_validationException() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanError_SeatsNotUnique();
        assertThrows(ValidHallplanExeption.class,
            () -> hallplanService.save(location.getId(), hallplan));
    }

    @Test
    public void test_seatRowNotOneCat_validationException() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanError_SeatRowNotOneCat();
        assertThrows(ValidHallplanExeption.class,
            () -> hallplanService.save(location.getId(), hallplan));
    }

    @Test
    public void test_seatCatInvalid_validationException() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanError_SeatCatInvalid();
        assertThrows(ValidHallplanExeption.class,
            () -> hallplanService.save(location.getId(), hallplan));
    }

    @Test
    public void test_areaNotUnique_validationException() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanError_AreaNotUnique();
        assertThrows(ValidHallplanExeption.class,
            () -> hallplanService.save(location.getId(), hallplan), hallplan.toString());
    }

    @Test
    public void test_areaCatInvalid_validationException() {
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanError_AreaCatInvalid();
        assertThrows(ValidHallplanExeption.class,
            () -> hallplanService.save(location.getId(), hallplan));
    }

}
