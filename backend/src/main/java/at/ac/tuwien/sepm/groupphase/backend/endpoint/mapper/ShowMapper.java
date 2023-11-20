package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanLocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ShowMapper {
    Show showDtoToEntity(ShowDto showDto);

    ShowDto showEntityToDto(Show show);

    List<ShowDto> showEntityToDto(List<Show> show);

    ShowSimpleDto showEntityToSimpleDto(Show show);

    List<ShowSimpleDto> showEntityToSimpleDto(List<Show> show);

    HallplanLocationDto hallplanEntityToLocationDto(Hallplan hallplan);
}
