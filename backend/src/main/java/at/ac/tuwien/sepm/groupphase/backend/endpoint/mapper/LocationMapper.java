package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface LocationMapper {
    LocationDto locationEntityToDto(Location location);

    List<LocationSimpleDto> locationEntityToSimpleDto(List<Location> locationDto);

    Location locationDtoToEntity(LocationDto locationDto);
}
