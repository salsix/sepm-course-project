package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Order(2)
@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;

    private final LocationHallplanEventCategoryEventShowTicketDataGenerator locationHallplanEventCategoryEventShowTicketDataGenerator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataGenerator(
        LocationHallplanEventCategoryEventShowTicketDataGenerator locationHallplanEventCategoryEventShowTicketDataGenerator,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder) {
        this.locationHallplanEventCategoryEventShowTicketDataGenerator = locationHallplanEventCategoryEventShowTicketDataGenerator;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUsers() {
        if (userRepository.findAll().size() > 0) {
            userRepository.deleteAll();
        }

        LOGGER.debug("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);

        List<ApplicationUser> users = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
            String name = i == 0 ? "admin" : "u" + i;
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setFirstName(name + "First");
            applicationUser.setLastName(name + "Last");
            applicationUser.setEmail(name + "@email.com");
            applicationUser.setPassword(passwordEncoder.encode("password"));
            applicationUser.setGender("Female");
            applicationUser.setDateOfBirth(new Date(0));
            applicationUser.setAdmin(i == 0);
            applicationUser.setLocked(false);
            applicationUser.setLoginFails(0);
            applicationUser.setPasswordReset(false);
            if (i != 0 && i % 3 == 0) {
                applicationUser.setLoginFails(5);
            }
            if (i != 0 && i % 5 == 0) {
                applicationUser.setLocked(true);
            }

            users.add(applicationUser);
        }
        userRepository.saveAll(users);

        locationHallplanEventCategoryEventShowTicketDataGenerator
            .generateLocationsHallplansEventCategoriesEventsShowsTickets(users);
    }

}
