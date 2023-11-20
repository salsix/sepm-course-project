package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {

    String[] IMAGE_TYPES = {"image/png", "image/jpeg"};

    /**
     * Finds all news entries.
     *
     * @return list of all news entries
     */
    List<News> getAllNews();

    /**
     * Finds all current news entries.
     *
     * @return list of all current news entries
     */
    List<News> getCurrentNews();

    /**
     * Finds all previous news entries.
     *
     * @return list of all previous news entries
     */
    List<News> getPreviousNews();

    /**
     * Finds a single news entry by id.
     *
     * @param id the id of the news entry
     * @return the news entry
     */
    News getById(Long id);

    /**
     * Saves a single news entry.
     *
     * @param news to save
     * @return published news entry
     */
    News save(News news);

    /**
     * Deletes a single news entry.
     *
     * @param id news to delete
     */
    void delete(Long id);

    /**
     * Saves one or more images to a news entry.
     *
     * @param newsId id of the news where the images should be saved
     * @param image  multipartFile with one or more image entries
     */
    void saveImages(Long newsId, MultipartFile[] image);

    /**
     * Loads a picture to a news entry.
     *
     * @param newsId id of the news where image should be loaded
     * @param imageIndex indexNumber of the image
     * @return image as a byteArray
     */
    byte[] getImages(Long newsId, int imageIndex);

    /**
     * Mark a news entry as read.
     *
     * @param newsId id of the news that should be marked as read
     */
    void newsRead(Long newsId);
}

