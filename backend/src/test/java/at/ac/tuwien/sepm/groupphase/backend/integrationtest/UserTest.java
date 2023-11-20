package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserTest implements TestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Value("${application-config.allowed-login-fails}")
    private int allowedLoginFails;


    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    public void test_getLockedUsers() {
        ApplicationUser normalUser = TestData.buildApplicationUser();
        userService.registerUser(normalUser);
        ApplicationUser lockedUser = TestData.buildApplicationUserLocked();
        userService.registerUser(lockedUser);
        ApplicationUser loginFailsMax = TestData.buildApplicationUserMaxLoginFails(allowedLoginFails);
        userService.registerUser(loginFailsMax);
        List<ApplicationUser> lockedUsers = userService.getLockedUsers();

        assertTrue(lockedUsers.contains(lockedUser) && lockedUsers.contains(loginFailsMax) && lockedUsers.size() == 2);

    }

    @Test
    public void test_registerUser() throws Exception {
        //save user
        ApplicationUserDto applicationUserDto = applicationUserMapper.applicationUserToDto(TestData.buildApplicationUser());
        String body = objectMapper.writeValueAsString(applicationUserDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(USER_BASE_URI + "/register")
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
    }


}
