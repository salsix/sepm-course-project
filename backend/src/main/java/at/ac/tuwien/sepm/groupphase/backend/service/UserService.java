package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailAlreadyRegisteredException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address.
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * register a new user.
     *
     * @param userEntity the user to register
     * @return the added application user
     * @throws EmailAlreadyRegisteredException is thrown if a user with the certain email is already registered
     * @throws ValidationException             is thrown if input is not valid
     */
    ApplicationUser registerUser(ApplicationUser userEntity);

    /**
     * update a specific user.
     *
     * @param user the user to update
     * @return the updated application user
     * @throws EmailAlreadyRegisteredException is thrown if a user with the certain email is already registered
     * @throws ValidationException             is thrown if input is not valid
     */
    ApplicationUser updateUser(ApplicationUser user);

    /**
     * get a specific user.
     *
     * @return the specific application user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    ApplicationUser getUser();

    /**
     * update the password of a specific user.
     *
     * @param encodedPassword the password which should be changed
     * @return the updated application user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    ApplicationUser updatePassword(String encodedPassword);

    /**
     * delete specific user.
     *
     * @return the deleted application user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    ApplicationUser deleteUser();

    /**
     * Increase the number of failed logins by one.
     *
     * @param email the email address
     */
    ApplicationUser increaseFailedLogins(String email);

    /**
     * Reset the number of failed logins to zero.
     *
     * @param email the email address
     */
    ApplicationUser unlockUser(String email);

    /**
     * Get a list of all locked users.
     */
    List<ApplicationUser> getLockedUsers();

    /**
     * Manually lock a user.
     *
     * @param email the email address
     */
    ApplicationUser lockUser(String email);

    /**
     * Reset the password of a user so that he must change it at the next login.
     *
     * @param email the email address
     */
    ApplicationUser resetUserPassword(String email);

    /**
     * Get all users.
     *
     * @param specification to filter by properties
     */
    List<ApplicationUser> getAll(Specification<ApplicationUser> specification, int page);
}
