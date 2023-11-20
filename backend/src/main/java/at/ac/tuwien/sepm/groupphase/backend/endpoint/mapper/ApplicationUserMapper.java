package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserMapper {

    private final PasswordEncoder passwordEncoder;

    public ApplicationUserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUserDto applicationUserToDto(ApplicationUser user) {
        if (user == null) return null;
        return new ApplicationUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
            user.getPassword(), user.getGender(), user.getDateOfBirth(), user.getAdmin(), user.getLocked(), user.getLoginFails(), user.isPasswordReset());
    }

    public ApplicationUser applicationUserToEntity(ApplicationUserDto userDto) {
        if (userDto == null) return null;
        return new ApplicationUser(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
            passwordEncoder.encode(userDto.getPassword()), userDto.getGender(), userDto.getDateOfBirth(),
            userDto.getAdmin(), userDto.getLocked(), userDto.getLoginFails(), userDto.isPasswordReset());
    }
}
