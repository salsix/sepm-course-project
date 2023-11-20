package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NewsTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        imageRepository.deleteAll();
        newsRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String login(ApplicationUser user) {
        ApplicationUser userPosted = userService.registerUser(user);
        UserDetails authUser = userService.loadUserByUsername(userPosted.getEmail());

        //login
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser,
            authUser.getPassword(), authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userUser = ((User) authentication.getPrincipal());
        List<String> roles = userUser.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        return jwtTokenizer.getAuthToken(user.getEmail(), roles);
    }

    /////////////////// getAll()
    @Test
    public void test_getAll_admin() throws Exception {

        News news = newsRepository.save(TestData.buildNews("NiceFilm"));
        newsRepository.save(TestData.buildNews("Nice anderer Film"));
        newsRepository.save(TestData.buildNews("Beste Schauschpieler im Film xy."));

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check size
        NewsDto[] newsDtos = objectMapper.readValue(response.getContentAsString(),
            NewsDto[].class);
        assertEquals(3, newsDtos.length);

        //check equal
        assertEquals(newsMapper.newsEntityToDto(news), newsDtos[0]);
    }

    @Test
    public void test_getAll_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI)
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getById()
    @Test
    public void test_getById() throws Exception {
        News news = TestData.buildNews("TestNews");
        news = newsRepository.save(news);

        //getById, user roles
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + news.getId()).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        NewsDto newsDto = objectMapper.readValue(response.getContentAsString(),
            NewsDto.class);
        assertEquals(newsMapper.newsEntityToDto(news), newsDto);
    }

    @Test
    public void test_getAllNews() throws Exception {

        newsRepository.save(TestData.buildNews("TestNews"));
        newsRepository.save(TestData.buildNews("TestNews2"));
        newsRepository.save(TestData.buildNews("TestNews3"));

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        NewsDto[] newsDtos = objectMapper.readValue(response.getContentAsString(),
            NewsDto[].class);
        assertEquals(3, newsDtos.length);

    }

    @Test
    public void test_getById_notFound() throws Exception {
        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/1")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_getById_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/1")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getImage()
    @Test
    public void test_getImage() throws Exception {

        News news = TestData.buildNews("TestNews_TestImage");
        news = newsRepository.save(news);

        byte[] image0 = {0, 2, 3, 4};
        byte[] image1 = {0, 2, 3, 4};
        imageRepository.save("news-" + news.getId() + "-0", image0);
        imageRepository.save("news-" + news.getId() + "-1", image0);

        MvcResult mvcResult0 = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + news.getId() + "/images/0").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response0 = mvcResult0.getResponse();

        MvcResult mvcResult1 = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + news.getId() + "/images/1").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();
        assertEquals(HttpStatus.OK.value(), response1.getStatus());
        response1.setCharacterEncoding("utf-8");

        byte[] res0 = response0.getContentAsByteArray();
        int length0 = image0.length;
        for (int i = 0; i < length0; ++i) {
            assertEquals(res0[i], image0[i]);
        }
        byte[] res1 = response1.getContentAsByteArray();
        int length1 = image1.length;
        for (int i = 0; i < length1; ++i) {
            assertEquals(res1[i], image1[i]);
        }
    }

    @Test
    public void test_getImage_notFound() throws Exception {

        long newsId = 1L;
        long picId = 1L;
        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + newsId + "/images/" + picId)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_save() throws Exception {

        NewsDto newsDto = newsMapper.newsEntityToDto(TestData.buildNews("TestNews"));
        String body = objectMapper.writeValueAsString(newsDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(NEWS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body).characterEncoding("utf-8")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)
                )
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        News news = objectMapper.readValue(response.getContentAsString(),
            News.class);
        assertNotNull(news.getId());
        newsDto.setPublishedAt(news.getPublishedAt());
        newsDto.setId(news.getId());
        assertEquals(newsDto, newsMapper.newsEntityToDto(news), news.toString());
    }

    @Test
    public void test_save_defaultUser_forbidden() throws Exception {
        NewsDto newsDto = newsMapper.newsEntityToDto(TestData.buildNews("TestNews"));
        String body = objectMapper.writeValueAsString(newsDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(NEWS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)
                )
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void test_saveMultipleImages_newsNotExist_notFound() throws Exception {
        MockMultipartFile mockMultipartFile = TestData.buildMockImagesNews();
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(NEWS_BASE_URI + "/1/images")
                .file(mockMultipartFile)
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_saveMultipleImages() throws Exception {

        News news = TestData.buildNews("TestNews_TestImage", 1L);
        news = newsRepository.save(news);

        MockMultipartFile mockMultipartFile = TestData.buildMockImagesNews();
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(NEWS_BASE_URI + "/" + news.getId() + "/images")
                .file(mockMultipartFile)
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    public void test_flagNewsAsRead() throws Exception {

        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        News news = TestData.buildNews("TestNews_TestImage", 1L);
        news = newsRepository.save(news);

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + news.getId() + "/read")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void test_flagNewsAsRead_wrongId_NotFound() throws Exception {

        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        News news = TestData.buildNews("TestNews_TestImage", 1L);
        newsRepository.save(news);
        long worngId = 100L;

        MvcResult mvcResult = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + worngId + "/read")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_currentNewsAndPreviousNews() throws Exception {

        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        News news = TestData.buildNews("News1", 1L);
        news = newsRepository.save(news);
        newsRepository.save(TestData.buildNews("News2"));
        newsRepository.save(TestData.buildNews("News3"));
        //mark #1 as read
        MvcResult mvcResultRead = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + news.getId() + "/read")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse responseRead = mvcResultRead.getResponse();
        assertEquals(HttpStatus.OK.value(), responseRead.getStatus());

        //request /current
        MvcResult mvcResultCurrent = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/current").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse responseCurrent = mvcResultCurrent.getResponse();
        assertEquals(HttpStatus.OK.value(), responseCurrent.getStatus());

        //check size
        NewsDto[] newsDtos = objectMapper.readValue(responseCurrent.getContentAsString(),
            NewsDto[].class);
        assertEquals(2, newsDtos.length);
        assertEquals(HttpStatus.OK.value(), responseCurrent.getStatus());

        //request /previous
        MvcResult mvcResultPrevious = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/previous").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse responsePrevious = mvcResultPrevious.getResponse();
        assertEquals(HttpStatus.OK.value(), responsePrevious.getStatus());

        //check size
        NewsDto[] newsDtosPrevious = objectMapper.readValue(responsePrevious.getContentAsString(),
            NewsDto[].class);
        assertEquals(1, newsDtosPrevious.length);
        assertEquals(HttpStatus.OK.value(), responsePrevious.getStatus());

    }

    @Test
    public void test_currentNewsAndPreviousNews_notSameLength() throws Exception {

        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        News news = TestData.buildNews("News1", 1L);
        news = newsRepository.save(news);
        newsRepository.save(TestData.buildNews("News2"));
        newsRepository.save(TestData.buildNews("News3"));
        //mark #1 as read
        this.mockMvc.perform(
            get(NEWS_BASE_URI + "/" + news.getId() + "/read")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();

        this.mockMvc.perform(
            get(NEWS_BASE_URI + "/current").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();

        //request /previous
        MvcResult mvcResultPrevious = this.mockMvc.perform(
            get(NEWS_BASE_URI + "/previous").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse responsePrevious = mvcResultPrevious.getResponse();
        assertEquals(HttpStatus.OK.value(), responsePrevious.getStatus());

        //check size
        NewsDto[] newsDtosPrevious = objectMapper.readValue(responsePrevious.getContentAsString(),
            NewsDto[].class);

        assertNotSame(0, newsDtosPrevious.length);
        assertEquals(HttpStatus.OK.value(), responsePrevious.getStatus());

    }
}