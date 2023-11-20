package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserMapper {
    public ApplicationUserUpdateDto applicationUserUpdateToDto(ApplicationUser user) {
        if (user == null) return null;
        return new ApplicationUserUpdateDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getGender(), user.getDateOfBirth());
    }

    public ApplicationUser applicationUserUpdateToEntity(ApplicationUserUpdateDto userUpdateDto) {
        if (userUpdateDto == null) return null;
        return new ApplicationUser(userUpdateDto.getId(), userUpdateDto.getFirstName(), userUpdateDto.getLastName(), userUpdateDto.getEmail(), userUpdateDto.getGender(), userUpdateDto.getDateOfBirth());
    }

    public ApplicationUser userPasswordDtoToUserEntity(UserPasswordDto userPasswordDto) {
        if (userPasswordDto == null) return null;
        return new ApplicationUser(userPasswordDto.getPassword());
    }
}
