package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthEventListener;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsServiceImpl implements NewsService {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private final NewsRepository newsRepository;
    private final ImageRepository imageRepository;
    private final AuthEventListener authEventListener;
    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, ImageRepository imageRepository,
                           AuthEventListener authEventListener, CustomUserDetailService customUserDetailService,
                           UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.imageRepository = imageRepository;
        this.authEventListener = authEventListener;
        this.userRepository = userRepository;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getPreviousNews() {
        LOGGER.trace("getPreviousNews()");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser = customUserDetailService.loadUserByUsername(auth.getName());
        ApplicationUser storedUser = customUserDetailService
            .findApplicationUserByEmail(authUser.getUsername());
        Long userId = storedUser.getId();
        return newsRepository.getPreviousNews(userId);
    }

    @Override
    public List<News> getCurrentNews() {
        LOGGER.trace("getCurrentNews()");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser = customUserDetailService.loadUserByUsername(auth.getName());
        ApplicationUser storedUser = customUserDetailService
            .findApplicationUserByEmail(authUser.getUsername());
        Long userId = storedUser.getId();
        return newsRepository.getCurrentNews(userId);
    }

    @Override
    public News getById(Long id) {
        LOGGER.trace("getById({})", id);
        return newsRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("News do not exist"));
    }

    @Override
    public News save(News news) {
        LOGGER.trace("save({})", news);
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LOGGER.trace("delete({})", id);
        News news = newsRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("News do not exist"));
        newsRepository.delete(news);
    }

    @Override
    @Transactional
    public void saveImages(Long newsId, MultipartFile[] image) {
        LOGGER.trace("enter imageSave({})", newsId);
        int pictureCount = 0;
        News news = newsRepository.findById(newsId)
            .orElseThrow(() -> new NotFoundException("News do not exist"));

        for (MultipartFile file : image) {
            LOGGER.trace("imageSave({},{})", newsId, file.getOriginalFilename());
            //check type
            String type = file.getContentType();
            if (type == null) {
                throw new ValidationException("Could not read image");
            }

            boolean matchingType = false;
            for (String imageType : IMAGE_TYPES) {
                if (type.equals(imageType)) {
                    matchingType = true;
                    break;
                }
            }
            if (!matchingType) {
                throw new ValidationException("Only .png and .jpeg allowed for image upload");
            }
            //save image
            byte[] bytes;
            try {
                bytes = file.getBytes();
                imageRepository.save("news-" + newsId + "-" + pictureCount, bytes);
                pictureCount++;
            } catch (IOException e) {
                //TODO: maybe another type of exception for this
                throw new ValidationException("Could not process image");
            }
            //update image in news
            newsRepository.save(news);
        }
        news.setImageCount(pictureCount);
    }

    @Override
    public byte[] getImages(Long newsId, int indexId) {
        LOGGER.trace("imageGet({})", newsId);
        News news = newsRepository.findById(newsId)
            .orElseThrow(() -> new NotFoundException("News do not exist"));

        return imageRepository.get("news-" + newsId + "-" + indexId);
    }

    @Override
    public void newsRead(Long newsId) {

        News news = newsRepository.findById(newsId)
            .orElseThrow(() -> new NotFoundException("News do not exist"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser = customUserDetailService.loadUserByUsername(auth.getName());
        ApplicationUser storedUser = customUserDetailService
            .findApplicationUserByEmail(authUser.getUsername());

        List<ApplicationUser> users = news.getWasReadByUsers();
        if (!users.contains(storedUser)) {
            news.getWasReadByUsers().add(storedUser);
            newsRepository.save(news);
        }
    }
}