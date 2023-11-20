package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallplanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.HallplanMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallplanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class HallplanTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private HallplanRepository hallplanRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HallplanMapper hallplanMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        hallplanRepository.deleteAll();
        locationRepository.deleteAll();
    }

    /////////////////// getById()
    @Test
    public void test_getById() throws Exception {
        //setup database
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //get request
        MvcResult mvcResult = this.mockMvc.perform(get(HALLPLAN_BASE_URI + "/" + hallplan.getId())
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check same data
        HallplanDto hallplanDto = objectMapper.readValue(response.getContentAsString(), HallplanDto.class);
        assertEquals(hallplanDto, hallplanMapper.hallplanEntityToDto(hallplan));
    }

    @Test
    public void test_getById_notFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(HALLPLAN_BASE_URI + "/1")
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus(),
            response.getContentAsString());
    }

    @Test
    public void test_getById_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(HALLPLAN_BASE_URI + "/1")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// save()
    @Test
    public void test_save() throws Exception {
        //save location
        Location location = locationRepository.save(TestData.buildLocation());

        //save hallplan
        HallplanDto hallplanDto = hallplanMapper.hallplanEntityToDto(TestData.buildHallplanValid());
        String body = objectMapper.writeValueAsString(hallplanDto);
        MvcResult mvcResult = this.mockMvc.perform(post(HALLPLAN_BASE_URI + "/" + location.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus(), "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //id not null
        Hallplan hallplan = objectMapper.readValue(response.getContentAsString(), Hallplan.class);
        assertNotNull(hallplan.getId());

        //check same data not useful, since ids are all set in Position/Area/Seats
    }

    @Test
    public void test_save_defaultUser_forbidden() throws Exception {
        //save location
        Hallplan hallplan = TestData.buildHallplan();
        String body = objectMapper.writeValueAsString(hallplan);

        //new
        MvcResult mvcResult = this.mockMvc.perform(
            post(HALLPLAN_BASE_URI + "/1")
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

    /////////////////// edit()
    @Test
    public void test_edit() throws Exception {
        //setup database
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);
        //hallplan.setLocation(null);

        //MvcResult MockHttpServletResponse

        //edit
        hallplan.setName("Lugner Kino neuer Name");
        String body = objectMapper.writeValueAsString(hallplan);
        MvcResult mvcResult = this.mockMvc.perform(put(HALLPLAN_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check same data
        HallplanDto hallplanDto = objectMapper.readValue(response.getContentAsString(), HallplanDto.class);
        assertEquals(hallplanDto, hallplanMapper.hallplanEntityToDto(hallplan));

    }

    @Test
    public void test_edit_notFound() throws Exception {
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setId(1L);
        String body = objectMapper.writeValueAsString(hallplan);
        MvcResult mvcResult = this.mockMvc.perform(put(HALLPLAN_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus(),
            response.getContentAsString());
    }

    @Test
    public void test_edit_defaultUser_forbidden() throws Exception {
        String body = objectMapper.writeValueAsString(TestData.buildHallplan());
        MvcResult mvcResult = this.mockMvc.perform(put(HALLPLAN_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// delete()
    @Test
    public void test_delete() throws Exception {
        //setup database
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplan();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //delete request
        MvcResult mvcResult = this.mockMvc.perform(delete(HALLPLAN_BASE_URI + "/" + hallplan.getId())
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());
        response.setCharacterEncoding("utf-8");

        //get nothing
        Optional<Hallplan> opt = hallplanRepository.findById(hallplan.getId());
        assertTrue(opt.isEmpty());
    }

    @Test
    public void test_delete_notFound() throws Exception {
        //delete request
        MvcResult mvcResult = this.mockMvc.perform(delete(HALLPLAN_BASE_URI + "/1")
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_delete_defaultUser_forbidden() throws Exception {
        //delete
        MvcResult mvcResult = this.mockMvc.perform(
            delete(HALLPLAN_BASE_URI + "/1")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)
                )
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }
}
