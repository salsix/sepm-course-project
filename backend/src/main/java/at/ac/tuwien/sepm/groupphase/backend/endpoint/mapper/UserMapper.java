package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    ApplicationUserDto applicationUserEntityToDto(ApplicationUser user);

    ApplicationUser applicationUserDtoToEntity(ApplicationUserDto userDto);

    List<ApplicationUserDto> applicationUserEntityListToDtoList(List<ApplicationUser> userList);
}
