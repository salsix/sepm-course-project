package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailAlreadyRegisteredException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CustomUserDetailServiceTest implements TestData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    public void test_updateUsersEmail_shouldBeDifferent() {
        //save user with email: mark23@gmail.com
        ApplicationUser user = TestData.buildApplicationUser();
        ApplicationUser userPosted = userService.registerUser(user);
        UserDetails authUser = userService.loadUserByUsername(userPosted.getEmail());
        //ApplicationUser updateUser = userService.findApplicationUserByEmail(authUser.getUsername());

        //login
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //update email to: newEmail@gmail.com
        ApplicationUser updatedUser = userService.updateUser(TestData.buildApplicationUser2());
        assertNotEquals(userPosted.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void test_userAlreadyRegistered() {
        ApplicationUser applicationUser = TestData.buildApplicationUser3();
        userService.registerUser(applicationUser);
        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.registerUser(applicationUser));
    }

    @Test
    public void test_incorrectBirthdate_validationException() {
        ApplicationUser applicationUser = TestData.buildApplicationUser3();
        applicationUser.setDateOfBirth(Date.valueOf("2015-02-02"));
        assertThrows(ValidationException.class, () -> userService.registerUser(applicationUser));
    }

    @Test
    public void test_loadUserByWrongUsername() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("noUser"));
    }

    @Test
    public void test_updatePassword() {
        ApplicationUser user = TestData.buildApplicationUser4();
        ApplicationUser userPosted = userService.registerUser(user); //password now: meGood12*
        UserDetails authUser = userService.loadUserByUsername(userPosted.getEmail());
        ApplicationUser updateUser = userService.findApplicationUserByEmail(authUser.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ApplicationUser updatedUser = userService.updatePassword("newPassword");
        assertNotEquals(userPosted.getPassword(), updatedUser.getPassword());
    }


}
