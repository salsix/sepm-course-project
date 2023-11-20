package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
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
public class ShowTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private HallplanRepository hallplanRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private ShowService showService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShowMapper showMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        showRepository.deleteAll();
        hallplanRepository.deleteAll();
        eventRepository.deleteAll();
        locationRepository.deleteAll();
        eventCategoryRepository.deleteAll();
    }

    /////////////////// getById()
    @Test
    public void test_getById() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save location & hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save show
        Show show = TestData.buildShow();
        show.setHallplan(hallplan);
        show = showService.save(event.getId(), show);
        show.setEvent(event);

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(SHOW_BASE_URI + "/" + show.getId()).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        ShowDto showDto = objectMapper.readValue(response.getContentAsString(),
            ShowDto.class);
        assertEquals(showMapper.showEntityToDto(show), showDto);
    }

    @Test
    public void test_getById_notFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(SHOW_BASE_URI + "/1")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_getById_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(SHOW_BASE_URI + "/1")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// save()
    @Test
    public void test_save() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save location & hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save show
        Show show = TestData.buildShow();
        show.setHallplan(hallplan);

        //save event
        String body = objectMapper.writeValueAsString(showMapper.showEntityToDto(show));
        show.setEvent(event);

        MvcResult mvcResult = this.mockMvc.perform(
            post(SHOW_BASE_URI + "/" + event.getId())
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
        ShowDto showDto = objectMapper.readValue(response.getContentAsString(),
            ShowDto.class);
        assertNotNull(showDto.getId());

        //check same data
        show.setId(showDto.getId());
        assertEquals(showMapper.showEntityToDto(show), showDto, showDto.toString());
    }

    @Test
    public void test_save_defaultUser_forbidden() throws Exception {
        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));

        //save event
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save location & hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save show
        Show show = TestData.buildShow();
        show.setHallplan(hallplan);

        //save event
        String body = objectMapper.writeValueAsString(showMapper.showEntityToDto(show));

        MvcResult mvcResult = this.mockMvc.perform(
            post(SHOW_BASE_URI + "/" + event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body).characterEncoding("utf-8")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)
                )
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
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save location & hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save show
        Show show = TestData.buildShow();
        show.setHallplan(hallplan);
        show = showService.save(event.getId(), show);

        //edit
        show.setHour(23);
        String body = objectMapper.writeValueAsString(showMapper.showEntityToDto(show));

        MvcResult mvcResult = this.mockMvc.perform(
            put(SHOW_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8")
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

        //save event
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save location & hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save show
        Show show = TestData.buildShow();
        show.setHallplan(hallplan);
        show.setId(2L);
        String body = objectMapper.writeValueAsString(showMapper.showEntityToDto(show));

        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            put(SHOW_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8")
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
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);

        //save location & hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save show
        Show show = TestData.buildShow();
        show.setHallplan(hallplan);
        show = showService.save(event.getId(), show);
        String body = objectMapper.writeValueAsString(showMapper.showEntityToDto(show));

        //edit
        MvcResult mvcResult = this.mockMvc.perform(
            put(SHOW_BASE_URI)
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
