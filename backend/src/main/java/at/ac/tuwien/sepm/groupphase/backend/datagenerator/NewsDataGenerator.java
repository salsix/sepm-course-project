package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_NEWS_TO_GENERATE = 5;
    private static final String TEST_NEWS_TITLE = "Title";
    private static final String TEST_NEWS_CONTENT = "Content";
    private static final String TEST_NEWS_SHORTDESCRIPTION = "Short-Description";
    private static final String RESOURCES_PATH = "src/main/resources";
    private final NewsRepository newsRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public NewsDataGenerator(NewsRepository newsRepository, ImageRepository imageRepository) {
        this.newsRepository = newsRepository;
        this.imageRepository = imageRepository;
    }

    @PostConstruct
    public void generateNews() {
        newsRepository.deleteAll();

        if (newsRepository.findAll().size() > 0) {
            newsRepository.deleteAll();
        }
        LOGGER.debug("generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);

        List<News> newsList = new ArrayList<>();
        /*for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
            News news = new News();
            news.setTitle(TEST_NEWS_TITLE + " " + i);
            news.setPublishedAt(LocalDateTime.now());
            news.setContent(TEST_NEWS_CONTENT + " " + i);
            news.setShortDescription(TEST_NEWS_SHORTDESCRIPTION + " " + i);
            news.setImageCount(0);
            newsList.add(news);
        }*/

        if (true) {
            News news = new News();
            news.setTitle("Jumanji released!");
            news.setPublishedAt(LocalDateTime.now());
            news.setContent("Jumanji is starring in cinemas today!\nJumanji is a 1995 American fantasy adventure film " +
                "directed by Joe Johnston. It is loosely based on the 1981 children's book by Chris Van Allsburg and the first installment of the " +
                "Jumanji franchise. The film was written by Van Allsburg, Greg Taylor, Jonathan Hensleigh, and Jim Strain and stars Robin " +
                "Williams, Kirsten Dunst, David Alan Grier, Bonnie Hunt, Jonathan Hyde, and Bebe Neuwirth.\n" +
                "\n" +
                "The story centers on a supernatural board game that releases jungle-based hazards upon its players with every turn " +
                "they take. As a boy in 1969, Alan Parrish became trapped inside the game itself while playing with his friend, " +
                "Sarah Whittle. Twenty-six years later, siblings Judy and Peter Shepherd find the game, begin playing and then " +
                "unwittingly release the now-adult Alan. After tracking down Sarah, the quartet resolves to finish " +
                "the game in order to reverse all of the destruction it has caused.");
            news.setShortDescription("Jumanji is starring in cinemas today!");
            news.setImageCount(3);
            newsList.add(news);

            news = new News();
            news.setTitle("Star Wars 8 released!");
            news.setPublishedAt(LocalDateTime.now());
            news.setContent("Star Wars 8 is starring in cinemas today!\nStar Wars: The Last Jedi (also known as Star Wars: Episode VIII – " +
                "The Last Jedi) is a 2017 American epic space opera film written and directed by Rian Johnson. P" +
                "roduced by Lucasfilm and distributed by Walt Disney Studios Motion Pictures, it is the second insta" +
                "llment of the Star Wars sequel trilogy, following The Force Awakens (2015), and the eighth episode " +
                "of the nine-part \"Skywalker saga\". The film's ensemble cast includes Mark Hamill, Carrie Fisher," +
                " Adam Driver, Daisy Ridley, John Boyega, Oscar Isaac, Andy Serkis, Lupita Nyong'o, Domhnall Gleeso" +
                "n, Anthony Daniels, Gwendoline Christie, Kelly Marie Tran, Laura Dern, and Benicio del Toro. The " +
                "Last Jedi follows Rey as she seeks the aid of Luke Skywalker, in hopes of turning the tide" +
                " for the Resistance in the fight against Kylo Ren and the First Order, while General Leia Organa" +
                ", Finn, and Poe Dameron attempt to escape a First Order attack on the dwindling Resistance fl" +
                "eet. The film features the first posthumous film performance by Fisher, who died in December 2016, and the film is dedicated to her.");
            news.setShortDescription("Star Wars 8 is starring in cinemas today!");
            news.setImageCount(2);
            newsList.add(news);

            news = new News();
            news.setTitle("Apollo just opened in 1060!");
            news.setPublishedAt(LocalDateTime.now());
            news.setContent("A new temple for movie lovers opened in Vienna's sixth district!\n" +
                "Apollo Cinema has great popcorn and offers attractive discounts for students." +
                "Come and watch the latest and greatest hollywood has to offer!");
            news.setShortDescription("A new cinema is in town.");
            news.setImageCount(0);
            newsList.add(news);
        }

        newsList = newsRepository.saveAll(newsList);

        try {
            News news = newsList.get(0);
            for (int i = 1; i <= 3; ++i) {
                byte[] bytes = Files.readAllBytes(Paths.get(RESOURCES_PATH + "/jumanji-" + i + ".jpg"));
                imageRepository.save("news-" + news.getId() + "-" + (i - 1), bytes);
            }

            news = newsList.get(1);
            for (int i = 1; i <= 2; ++i) {
                byte[] bytes = Files.readAllBytes(Paths.get(RESOURCES_PATH + "/starwars-" + i + ".jpg"));
                imageRepository.save("news-" + news.getId() + "-" + (i - 1), bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not save images to news " + e);
        }
    }

}
