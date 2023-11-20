package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(NewsEndpoint.BASE_URL)
public class NewsEndpoint {

    static final String BASE_URL = "/api/v1/news";
    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @Autowired
    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "")
    @Operation(summary = "get all news ")
    public List<NewsDto> getAllNews() {
        LOGGER.info("GET " + BASE_URL);
        return newsMapper.newsEntityToDto(newsService.getAllNews());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/current")
    @Operation(summary = "get all current news ")
    public List<NewsDto> getCurrentNews() {
        LOGGER.info("GET " + BASE_URL + "/current");
        return newsMapper.newsEntityToDto(newsService.getCurrentNews());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/previous")
    @Operation(summary = "get all previous news ")
    public List<NewsDto> getPrevNews() {
        LOGGER.info("GET " + BASE_URL + "/previous");
        return newsMapper.newsEntityToDto(newsService.getPreviousNews());
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/{id}")
    @Operation(summary = "get news with all infos with given id")
    NewsDto getById(@PathVariable Long id) {
        LOGGER.info("GET " + BASE_URL + "/{}", id);
        return newsMapper.newsEntityToDto(newsService.getById(id));
    }

    @PermitAll
    @GetMapping(value = "/{newsId}/images/{imageIndex}", produces = {"image/png", "image/jpeg"})
    @Operation(summary = "gets news image by news id and imageIndex")
    Resource getImage(@PathVariable("newsId") Long newsId, @PathVariable int imageIndex) {
        LOGGER.info("GET " + BASE_URL + "/{}/images {}/imageIndex", newsId, imageIndex);
        byte[] imageList = newsService.getImages(newsId, imageIndex);
        return new ByteArrayResource(imageList);
    }

    @PermitAll
    @PostMapping(value = "/{newsId}/images")
    @Operation(summary = "save new image to existing news")
    void saveImage(@PathVariable("newsId") Long newsId,
        @RequestParam("images") MultipartFile[] images) {
        LOGGER.info("POST " + BASE_URL + "/{}/images ", newsId);
        newsService.saveImages(newsId, images);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping()
    @Operation(summary = "save new news")
    @ResponseStatus(HttpStatus.CREATED)
    NewsDto save(@Valid @RequestBody NewsDto newsDto) {
        LOGGER.info("POST " + BASE_URL + " new: {}: ", newsDto);
        return newsMapper.newsEntityToDto(newsService.save(newsMapper.newsDtoToEntity(newsDto)));
    }

    @PermitAll
    @GetMapping("/{newsId}/read")
    @Operation(summary = "flag read news")
    void newsRead(@NotNull @PathVariable("newsId") Long newsId) {
        LOGGER.info("Get " + BASE_URL + "/{}/newsId was read", newsId);
        newsService.newsRead(newsId);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "delete news")
    public void delete(@NotNull @PathVariable("id") Long id) {
        LOGGER.info("DELETE " + BASE_URL + "/{}", id);
        newsService.delete(id);
    }

}
