package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface NewsMapper {
    News newsDtoToEntity(NewsDto newsDto);

    NewsDto newsEntityToDto(News news);

    List<NewsDto> newsEntityToDto(List<News> newsDto);
}
