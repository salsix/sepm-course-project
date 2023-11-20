package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanLocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopTenDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopTen;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    Event eventDtoToEntity(EventDto eventDto);

    EventSimpleDto eventEntityToSimpleDto(Event event);

    List<EventSimpleDto> eventEntityToSimpleDto(List<Event> event);

    EventDto eventEntityToDto(Event event);

    HallplanLocationDto hallplanEntityToLocationDto(Hallplan hallplan);

    TopTenDto topTenEntityToDto(TopTen topTen);

    List<TopTenDto> topTenEntityToDto(List<TopTen> topTens);
}
