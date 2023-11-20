package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationUserMapperTest implements TestData {

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @Test
    public void test_givenNothing_whenMapApplicationUserDtoToEntity() {
        ApplicationUserDto applicationUserDto = applicationUserMapper.applicationUserToDto(TestData.buildApplicationUser());
        ApplicationUser user = TestData.buildApplicationUser();
        assertAll(
            () -> assertEquals(user.getFirstName(), applicationUserDto.getFirstName()),
            () -> assertEquals(user.getLastName(), applicationUserDto.getLastName()),
            () -> assertEquals(user.getEmail(), applicationUserDto.getEmail()),
            () -> assertEquals(user.getGender(), applicationUserDto.getGender()),
            () -> assertEquals(user.getDateOfBirth(), applicationUserDto.getDateOfBirth())
        );
    }

    @Test
    public void test_givenNothing_whenMapApplicationUserEntityTo() {
        ApplicationUser applicationUser = applicationUserMapper.applicationUserToEntity(TestData.buildApplicationUser1());
        ApplicationUserDto user = TestData.buildApplicationUser1();
        assertAll(
            () -> assertEquals(user.getFirstName(), applicationUser.getFirstName()),
            () -> assertEquals(user.getLastName(), applicationUser.getLastName()),
            () -> assertEquals(user.getEmail(), applicationUser.getEmail()),
            () -> assertEquals(user.getGender(), applicationUser.getGender()),
            () -> assertEquals(user.getDateOfBirth(), applicationUser.getDateOfBirth())
        );
    }
}
