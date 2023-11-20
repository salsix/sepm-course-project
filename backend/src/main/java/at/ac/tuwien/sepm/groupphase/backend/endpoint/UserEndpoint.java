package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UpdateUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.specification.ApplicationUserSpecification;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(UserEndpoint.BASE_URL)
public class UserEndpoint {
    static final String BASE_URL = "/api/v1/users";
    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UpdateUserMapper updateUserMapper;
    private final UserMapper userMapper;
    private final ApplicationUserMapper applicationUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserEndpoint(UserService userService, UpdateUserMapper updateUserMapper, UserMapper userMapper, ApplicationUserMapper applicationUserMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.updateUserMapper = updateUserMapper;
        this.userMapper = userMapper;
        this.applicationUserMapper = applicationUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Secured("ROLE_USER")
    @PutMapping(value = "/edit")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update the current user")
    public ApplicationUserUpdateDto updateUser(@Valid @RequestBody ApplicationUserUpdateDto userUpdateDto) {
        LOGGER.info("PUT " + BASE_URL + "/{}", userUpdateDto);
        ApplicationUser user = updateUserMapper.applicationUserUpdateToEntity(userUpdateDto);
        return updateUserMapper.applicationUserUpdateToDto(userService.updateUser(user));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/userdata")
    @Operation(summary = "gets the current user")
    public ApplicationUserUpdateDto getUser() {
        LOGGER.info("GET " + BASE_URL);
        return updateUserMapper.applicationUserUpdateToDto(userService.getUser());
    }

    @Secured({"ROLE_USER", "PW_RESET"})
    @PutMapping(value = "/password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update the password of the current user")
    public ApplicationUser updatePassword(@Valid @RequestBody UserPasswordDto userPasswordDto) {
        LOGGER.info("PUT " + BASE_URL + "/{}", userPasswordDto);
        String encodedPassword = passwordEncoder.encode(userPasswordDto.getPassword());
        return userService.updatePassword(encodedPassword);
    }

    @Secured("ROLE_USER")
    @DeleteMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete the current user")
    public ApplicationUserDto deleteUser() {
        LOGGER.info("Delete User" + BASE_URL);
        return applicationUserMapper.applicationUserToDto(userService.deleteUser());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/locked")
    @Operation(summary = "Get a list of all locked users")
    public List<ApplicationUserDto> getLockedUsers() {
        LOGGER.info("GET /api/v1/users/locked");
        return userMapper.applicationUserEntityListToDtoList(userService.getLockedUsers());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "")
    @Operation(summary = "Get a list of all users with the specified properties")
    public List<ApplicationUserDto> getUsers(ApplicationUserSpecification applicationUserSpecification, @RequestParam("page") int page) {
        LOGGER.info("GET /api/v1/users");
        return userMapper.applicationUserEntityListToDtoList(userService.getAll(applicationUserSpecification, page));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/unlock")
    @Operation(summary = "Unlock a user")
    public ApplicationUser unlockUser(@RequestBody String email) {
        LOGGER.info("POST /api/v1/users/unlock ({})", email);
        return userService.unlockUser(email);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Admin creates a new user")
    public ApplicationUserDto createNewUser(@Valid @RequestBody ApplicationUserDto user) {
        LOGGER.info("POST " + BASE_URL + "/{}", user);
        ApplicationUser userEntity = applicationUserMapper.applicationUserToEntity(user);
        return applicationUserMapper.applicationUserToDto(userService.registerUser(userEntity));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/locked")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Lock User")
    public ApplicationUser lockUser(@RequestBody String email) {
        LOGGER.info("POST /api/v1/users/locked ({})", email);
        return userService.lockUser(email);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/resetpassword")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Lock User")
    public ApplicationUser resetUserPassword(@RequestBody String email) {
        LOGGER.info("POST /api/v1/users/resetoassword ({})", email);
        return userService.resetUserPassword(email);
    }

    @PermitAll
    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers a new user")
    public ApplicationUserDto register(@Valid @RequestBody ApplicationUserDto user) {
        LOGGER.info("POST " + BASE_URL + " new: {}", user);
        ApplicationUser userEntity = applicationUserMapper.applicationUserToEntity(user);
        return applicationUserMapper.applicationUserToDto(userService.registerUser(userEntity));
    }
}
