package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    @Value("${application-config.allowed-login-fails}")
    private int allowedLoginFails;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);
            if (applicationUser.getLoginFails() >= allowedLoginFails) {
                // This exception is automatically wrapped in an InternalAuthenticationServiceException by Spring Security.
                throw new UserLockedException("Your account is locked. Please contact an administrator.");
            }

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else if (applicationUser.isPasswordReset()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("PW_RESET");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("Could not find the user with the email address %s", email)));
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }


    private void validateRegisterUser(ApplicationUser user) {
        Date date = user.getDateOfBirth();
        LocalDate localDateTime = date.toLocalDate();
        if (localDateTime.plusYears(13).isAfter(LocalDate.now())) {
            throw new ValidationException("You must be older to get registered");
        }
    }

    @Override
    public ApplicationUser registerUser(ApplicationUser user) {
        LOGGER.trace("registerUser({})", user);
        validateRegisterUser(user);
        Optional<ApplicationUser> storedUser = userRepository.findByEmail(user.getEmail());
        if (storedUser.isPresent()) {
            throw new EmailAlreadyRegisteredException("The Email is already taken");
        }
        return userRepository.save(user);
    }


    public ApplicationUser updateUser(ApplicationUser user) {
        LOGGER.trace("update({})", user);

        //validate new data
        validateRegisterUser(user);

        //get current logged in info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser = loadUserByUsername(auth.getName());

        //updateUser = this user, update fields
        ApplicationUser updateUser = findApplicationUserByEmail(authUser.getUsername());
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());

        //want to update email
        if (!updateUser.getEmail().equals(user.getEmail())) {
            //check email available
            Optional<ApplicationUser> storedUser = userRepository.findByEmail(user.getEmail());
            if (storedUser.isPresent()) {
                throw new EmailAlreadyRegisteredException("The Email is already taken");
            }
            //update email
            updateUser.setEmail(user.getEmail());
        }

        updateUser.setGender(user.getGender());
        updateUser.setDateOfBirth(user.getDateOfBirth());
        return userRepository.save(updateUser);
    }

    @Override
    public ApplicationUser getUser() {
        LOGGER.trace("get User");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = loadUserByUsername(auth.getName());
        return userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NotFoundException("User does not exist."));
    }

    @Override
    public List<ApplicationUser> getAll(Specification<ApplicationUser> specification, int page) {
        LOGGER.trace("get all Users");

        Pageable pageable = PageRequest.of(page, 5);

        return userRepository.findAll(specification, pageable);
    }

    @Override
    public ApplicationUser updatePassword(String encodedPassword) {
        LOGGER.trace("update password");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser = loadUserByUsername(auth.getName());
        ApplicationUser storedUser = findApplicationUserByEmail(authUser.getUsername());
        storedUser.setPassword(encodedPassword);
        storedUser.setPasswordReset(false);
        return userRepository.save(storedUser);
    }

    @Override
    public ApplicationUser deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser = loadUserByUsername(auth.getName());
        ApplicationUser storedUser = findApplicationUserByEmail(authUser.getUsername());

        storedUser.setEmail("");
        userRepository.save(storedUser);
        return storedUser;
    }

    @Override
    @Transactional
    public ApplicationUser increaseFailedLogins(String email) {
        LOGGER.trace("increaseFailedLogins({})", email);
        ApplicationUser user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setLoginFails(user.getLoginFails() + 1);
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ApplicationUser unlockUser(String email) {
        LOGGER.trace("unlockUser({})", email);
        ApplicationUser user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("Could not find the user with the email %s", email)));
        user.setLoginFails(0);
        user.setLocked(false);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public ApplicationUser lockUser(String email) {
        LOGGER.trace("lockUser({})", email);
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(email)) {
            throw new SelfLockException("You cannot lock your own account.");
        }
        ApplicationUser user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("Could not find the user with the email %s", email)));
        user.setLocked(true);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public List<ApplicationUser> getLockedUsers() {
        LOGGER.trace("getLockedUsers()");
        List<ApplicationUser> lockedUsers = userRepository.findByLoginFails(allowedLoginFails);
        lockedUsers.addAll(userRepository.findByLocked(true));
        return lockedUsers;
    }

    @Override
    @Transactional
    public ApplicationUser resetUserPassword(String email) {
        LOGGER.trace("resetUserPassword({})", email);
        ApplicationUser user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("Could not find the user with the email %s", email)));
        user.setPasswordReset(true);
        return userRepository.save(user);
    }
}
