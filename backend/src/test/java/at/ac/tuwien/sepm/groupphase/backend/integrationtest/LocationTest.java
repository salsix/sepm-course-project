package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LocationTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        locationRepository.deleteAll();
    }

    /////////////////// getAll()
    @Test
    public void test_getAll() throws Exception {
        //save location
        Location location = locationRepository.save(TestData.buildLocation());
        locationRepository.save(TestData.buildLocation());
        locationRepository.save(TestData.buildLocation());

        //getById, user roles
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check size
        LocationDto[] locationDto = objectMapper.readValue(response.getContentAsString(),
            LocationDto[].class);
        assertEquals(3, locationDto.length);

        //check equal
        location.setHallplans(null);
        assertEquals(locationMapper.locationEntityToDto(location), locationDto[0]);
    }

    @Test
    public void test_getAll_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI)
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getById()
    @Test
    public void test_getById() throws Exception {
        //save location
        Location location = TestData.buildLocation();
        location = locationRepository.save(location);

        //getById, user roles
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/" + location.getId()).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        LocationDto locationDto = objectMapper.readValue(response.getContentAsString(),
            LocationDto.class);
        assertEquals(locationMapper.locationEntityToDto(location), locationDto);
    }

    @Test
    public void test_getById_notFound() throws Exception {
        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/1")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_getById_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/1")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getCountries()
    @Test
    public void test_getCountries() throws Exception {
        List<Location> list = TestData.buildLocationList();
        locationRepository.saveAll(list);

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        String[] countries = objectMapper.readValue(response.getContentAsString(),
            String[].class);

        assertEquals(2, countries.length);
        assertEquals("Deutschland", countries[0]);
        assertEquals("Österreich", countries[1]);
    }

    @Test
    public void test_getCountries_nothing() throws Exception {
        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        String[] countries = objectMapper.readValue(response.getContentAsString(),
            String[].class);

        assertEquals(0, countries.length);
    }

    @Test
    public void test_getCountries_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getCountryZips()
    @Test
    public void test_getCountryZips() throws Exception {
        List<Location> list = TestData.buildLocationList();
        locationRepository.saveAll(list);

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries/Österreich/zips").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        String[] zips = objectMapper.readValue(response.getContentAsString(),
            String[].class);
        assertEquals(2, zips.length);
        assertEquals("1010", zips[0]);
        assertEquals("1070", zips[1]);
    }

    @Test
    public void test_getCountryZips_nothing() throws Exception {
        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries/Österreich/zips").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        String[] zips = objectMapper.readValue(response.getContentAsString(),
            String[].class);

        assertEquals(0, zips.length);
    }

    @Test
    public void test_getCountryZips_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries/Österreich/zips")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// getCountryZipLocations()
    @Test
    public void test_getCountryZipLocations() throws Exception {
        List<Location> list = TestData.buildLocationList();
        locationRepository.saveAll(list);

        //request
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries/Österreich/zips/1070/locations").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        Location[] locs = objectMapper.readValue(response.getContentAsString(),
            Location[].class);
        assertEquals(2, locs.length);
        assertEquals("Haydn Kino", locs[0].getName());
        assertEquals("Haydn Kino 2", locs[1].getName());
    }

    @Test
    public void test_getCountryZipLocations_nothing() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries/Österreich/zips/1070/locations").characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check equal
        Location[] locs = objectMapper.readValue(response.getContentAsString(),
            Location[].class);
        assertEquals(0, locs.length);
    }

    @Test
    public void test_getCountryZipLocations_noUser_forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get(LOCATION_BASE_URI + "/countries/Österreich/zips/1070/locations")
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    /////////////////// save()
    @Test
    public void test_save() throws Exception {
        //save location
        LocationDto locationDto = locationMapper.locationEntityToDto(TestData.buildLocation());
        String body = objectMapper.writeValueAsString(locationDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(LOCATION_BASE_URI)
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
        Location location = objectMapper.readValue(response.getContentAsString(),
            Location.class);
        assertNotNull(location.getId());

        //check same data
        locationDto.setId(location.getId());
        assertEquals(locationDto, locationMapper.locationEntityToDto(location), location.toString());
    }

    @Test
    public void test_save_defaultUser_forbidden() throws Exception {
        //save location
        LocationDto locationDto = locationMapper.locationEntityToDto(TestData.buildLocation());
        String body = objectMapper.writeValueAsString(locationDto);

        //new
        MvcResult mvcResult = this.mockMvc.perform(
            post(LOCATION_BASE_URI)
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
        //save location
        Location location = TestData.buildLocation();
        location = locationRepository.save(location);

        //edit
        location.setCountry("some country test");
        String body = objectMapper.writeValueAsString(location);

        MvcResult mvcResult = this.mockMvc.perform(
            put(LOCATION_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        response.setCharacterEncoding("utf-8");

        //check equal
        assertEquals(location, locationRepository.findByIdWithHallplans(location.getId()).orElseThrow(() -> new NotFoundException("Location not found")));
    }

    @Test
    public void test_edit_notFound() throws Exception {
        Location location = TestData.buildLocation();
        location.setId(1L);
        String body = objectMapper.writeValueAsString(location);

        //get not found
        MvcResult mvcResult = this.mockMvc.perform(
            put(LOCATION_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_edit_defaultUser_forbidden() throws Exception {
        //save location
        LocationDto locationDto = locationMapper.locationEntityToDto(TestData.buildLocation());
        String body = objectMapper.writeValueAsString(locationDto);

        //edit
        MvcResult mvcResult = this.mockMvc.perform(
            put(LOCATION_BASE_URI)
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

    /////////////////// delete()
    @Test
    public void test_delete() throws Exception {
        //save location
        Location location = TestData.buildLocation();
        location = locationRepository.save(location);

        //delete
        MvcResult mvcResult = this.mockMvc.perform(
            delete(LOCATION_BASE_URI + "/" + location.getId())
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //get nothing
        Optional<Location> opt = locationRepository.findById(location.getId());
        assertTrue(opt.isEmpty());
    }

    @Test
    public void test_delete_notFound() throws Exception {
        //delete
        MvcResult mvcResult = this.mockMvc.perform(
            delete(LOCATION_BASE_URI + "/1")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void test_delete_defaultUser_forbidden() throws Exception {
        //delete
        MvcResult mvcResult = this.mockMvc.perform(
            delete(LOCATION_BASE_URI + "/1")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)
                )
        ).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }


}
