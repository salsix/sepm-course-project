package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EventTest implements TestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        imageRepository.deleteAll();
        eventRepository.deleteAll();
        eventCategoryRepository.deleteAll();
    }

    /////////////////// getAll()
    @Test
    public void test_getAll() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = eventRepository.save(TestData.buildEvent("Jumanji", eventCategory));
        eventRepository.save(TestData.buildEvent("Jumanji 2", eventCategory));
        eventRepository.save(TestData.buildEvent("Jumanji 3", eventCategory));

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check size
        EventDto[] eventDtos = objectMapper.readValue(response.getContentAsString(),
            EventDto[].class);
        assertEquals(3, eventDtos.length);

        //check equal
        EventDto compare = eventMapper.eventEntityToDto(event);
        compare.setShows(null);
        assertEquals(compare, eventDtos[0]);
    }

    @Test
    public void test_getAll_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI)
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getById()
    @Test
    public void test_getById() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji");
        event.setCategory(eventCategory);
        event = eventRepository.save(event);

        //getById, user roles
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI + "/" + event.getId()).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        EventDto eventDto = objectMapper.readValue(response.getContentAsString(),
            EventDto.class);
        assertEquals(eventMapper.eventEntityToDto(event), eventDto);
    }

    @Test
    public void test_getById_notFound() throws Exception {
        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI + "/1")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_getById_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI + "/1")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getImage()
    @Test
    public void test_getImage() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save image
        byte[] image = {0, 2, 3, 4};
        imageRepository.save("event-" + event.getId(), image);

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI + "/" + event.getId() + "/image").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        response.setCharacterEncoding("utf-8");

        //check equal
        byte[] res = response.getContentAsByteArray();
        int length = image.length;
        for (int i = 0; i < length; ++i) {
            assertEquals(res[i], image[i]);
        }
    }

    @Test
    public void test_getImage_notFound() throws Exception {
        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            get(EVENT_BASE_URI + "/1/image")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /////////////////// save()
    @Test
    public void test_save() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        EventDto eventDto = eventMapper.eventEntityToDto(TestData.buildEvent("Jumanji"));
        eventDto.setCategory(eventCategory);
        String body = objectMapper.writeValueAsString(eventDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(EVENT_BASE_URI)
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

        //check location id set
        Event event = objectMapper.readValue(response.getContentAsString(),
            Event.class);
        assertNotNull(event.getId());

        //check same data
        eventDto.setId(event.getId());
        assertEquals(eventDto, eventMapper.eventEntityToDto(event), event.toString());
    }

    @Test
    public void test_save_defaultUser_forbidden() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        EventDto eventDto = eventMapper.eventEntityToDto(TestData.buildEvent("Jumanji"));
        eventDto.setCategory(eventCategory);
        String body = objectMapper.writeValueAsString(eventDto);

        //new
        MvcResult mvcResult = this.mockMvc.perform(
            post(EVENT_BASE_URI)
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

    /////////////////// saveImage()
    @Test
    public void test_saveImage() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);
        assertNotNull(event.getId());

        //request
        MockMultipartFile mockMultipartFile = TestData.buildMockImage();
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(EVENT_BASE_URI + "/" + event.getId() + "/image")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus(), response.getErrorMessage());

        //check equal
        byte[] res = imageRepository.get("event-" + event.getId());
        int length = mockMultipartFile.getBytes().length;
        for (int i = 0; i < length; ++i) {
            assertEquals(res[i], mockMultipartFile.getBytes()[i]);
        }
    }

    @Test
    public void test_saveImage_eventNotExists_notFound() throws Exception {
        MockMultipartFile mockMultipartFile = TestData.buildMockImage();
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(EVENT_BASE_URI + "/1/image")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_saveImage_defaultUser_forbidden() throws Exception {
        MockMultipartFile mockMultipartFile = TestData.buildMockImage();
        MvcResult mvcResult = this.mockMvc.perform(
            multipart(EVENT_BASE_URI + "/1/image")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// edit()
    @Test
    public void test_edit() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji");
        event.setCategory(eventCategory);
        event = eventRepository.save(event);

        //edit
        event.setName("Doch nicht Jumanji");
        String body = objectMapper.writeValueAsString(event);

        MvcResult mvcResult = this.mockMvc.perform(
            put(EVENT_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void test_edit_notFound() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        Event event = TestData.buildEvent("Jumanji");
        event.setCategory(eventCategory);
        event.setId(1L);
        String body = objectMapper.writeValueAsString(event);

        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            put(EVENT_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_edit_defaultUser_forbidden() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        EventDto eventDto = eventMapper.eventEntityToDto(TestData.buildEvent("Jumanji"));
        eventDto.setCategory(eventCategory);
        String body = objectMapper.writeValueAsString(eventDto);

        //edit
        MvcResult mvcResult = this.mockMvc.perform(
            put(EVENT_BASE_URI)
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
}
