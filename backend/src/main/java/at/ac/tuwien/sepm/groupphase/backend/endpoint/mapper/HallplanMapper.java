package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanLocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import org.mapstruct.Mapper;

@Mapper
public interface HallplanMapper {
    Hallplan hallplanDtoToEntity(HallplanDto hallplanDto);

    HallplanDto hallplanEntityToDto(Hallplan hallplan);

    HallplanSimpleDto hallplanEntityToSimpleDto(Hallplan hallplan);

    HallplanLocationDto hallplanEntityToLocationDto(Hallplan hallplan);
}
