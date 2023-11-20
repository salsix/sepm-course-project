package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookedDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booked;
import org.mapstruct.Mapper;

@Mapper
public interface BookedMapper {
    Booked dtoToEntity(BookedDto bookedDto);

    BookedDto entityToDto(Booked booked);
}
