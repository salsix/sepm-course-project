package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookedDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booked;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedSeat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallplanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TicketServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
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
//@ActiveProfiles("test")
@ActiveProfiles("testImpl")
@AutoConfigureMockMvc
public class TicketTest implements TestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private HallplanRepository hallplanRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private EventCategoryRepository eventCategoryRepository;
    @Autowired
    private TicketServiceImpl ticketService;

    //with hallplan: max 20 seats, max 160 area tickets
    private Show saveLocationHallplanEventShow() {
        //save location, hallplan
        Location location = locationRepository.save(TestData.buildLocation());
        Hallplan hallplan = TestData.buildHallplanValid();
        hallplan.setLocation(location);
        hallplan = hallplanRepository.save(hallplan);

        //save category
        EventCategory eventCategory = eventCategoryRepository.save(new EventCategory("Movie"));
        //save event, show
        Event event = TestData.buildEvent("Jumanji", eventCategory);
        event = eventRepository.save(event);
        Show show = TestData.buildShow();
        show.setEvent(event);
        show.setHallplan(hallplan);
        return showRepository.save(show);
    }

    @BeforeEach
    public void beforeEach() {
        billRepository.deleteAll();

        showRepository.deleteAll();
        eventRepository.deleteAll();
        eventCategoryRepository.deleteAll();

        hallplanRepository.deleteAll();
        locationRepository.deleteAll();

        userRepository.deleteAll();
    }

    private String login(ApplicationUser user) {
        ApplicationUser userPosted = userService.registerUser(user);
        UserDetails authUser = userService.loadUserByUsername(userPosted.getEmail());

        //login
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userUser = ((User) authentication.getPrincipal());
        List<String> roles = userUser.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        return jwtTokenizer.getAuthToken(user.getEmail(), roles);
    }

    @Test
    public void test_getBills() throws Exception {
        //setup database
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //buy tickets
        billRepository.save(BillMapper.bookedToBill(booked, show, user, false));
        billRepository.save(BillMapper.bookedToBill(booked, show, user, false));
        billRepository.save(BillMapper.bookedToBill(booked, show, user, false));

        //get request
        MvcResult mvcResult = this.mockMvc.perform(get(TICKET_BASE_URI)
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check same data
        BillSimpleDto[] bills = objectMapper.readValue(response.getContentAsString(), BillSimpleDto[].class);
        assertEquals(3, bills.length);
    }

    @Test
    public void test_getSingleBill() throws Exception {
        //setup database
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);
        booked.getAreas().put("S1", 5);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //buy tickets
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, false));

        //get request
        MvcResult mvcResult = this.mockMvc.perform(get(TICKET_BASE_URI + "/" + bill.getId())
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");

        //check same data
        BillDto billDto = objectMapper.readValue(response.getContentAsString(), BillDto.class);
        BookedDto b = billDto.getWant();
        assertEquals(false, billDto.getReserved(), billDto.toString());
        assertTrue(b.getSeats().containsKey("R1"), billDto.toString());
        assertTrue(b.getSeats().get("R1").contains(1), billDto.toString());
        assertTrue(b.getSeats().get("R1").contains(2), billDto.toString());
        assertEquals(5, b.getAreas().get("S1"), billDto.toString());
    }

    @Test
    public void test_buy() throws Exception {
        //setup database
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //buy tickets
        String body = objectMapper.writeValueAsString(booked);
        MvcResult mvcResult = this.mockMvc.perform(post(TICKET_BASE_URI + "/buy")
            .contentType(MediaType.APPLICATION_JSON).content(body)
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());

        Bill bill = billRepository.findAllByApplicationUserEager(user).get(0);
        assertEquals(2, bill.getSeats().size());

        for (BookedSeat s : bill.getSeats()) {
            assertEquals("R1", s.getIdentifier());
            assertTrue(s.getSeat() == 1 || s.getSeat() == 2);
        }
    }

    @Test
    public void test_reserve() throws Exception {
        //setup database
        Show show = saveLocationHallplanEventShow();
        Booked booked = TestData.buildBooked(show, 5, 0);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //reserve tickets
        String body = objectMapper.writeValueAsString(booked);
        MvcResult mvcResult = this.mockMvc.perform(post(TICKET_BASE_URI + "/reserve")
            .contentType(MediaType.APPLICATION_JSON).content(body)
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());
    }

    @Test
    public void test_storno() throws Exception {
        //setup database
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);
        String body = objectMapper.writeValueAsString(booked);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //buy tickets
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, false));

        //storno
        MvcResult mvcResult = this.mockMvc.perform(delete(TICKET_BASE_URI + "/" + bill.getId())
            .contentType(MediaType.APPLICATION_JSON).content(body)
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());

        //can buy tickets again
        mvcResult = this.mockMvc.perform(post(TICKET_BASE_URI + "/buy")
            .contentType(MediaType.APPLICATION_JSON).content(body)
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus(), "Response: " + response.getContentAsString());

    }

    @Test
    public void test_buyReserved() throws Exception {
        //setup database
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //buy tickets
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, true));

        list.remove(1);
        String body = objectMapper.writeValueAsString(booked);
        //can buy tickets again
        MvcResult mvcResult = this.mockMvc
            .perform(post(TICKET_BASE_URI + "/" + bill.getId() + "/buy")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus(),
            "Response: " + response.getContentAsString());

    }

    @Test
    public void test_userGetBillPdf() throws Exception {
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, true));

        //get request
        MvcResult getBillPdfResult = this.mockMvc
            .perform(get(TICKET_BASE_URI + "/" + bill.getId() + "/billPdf")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = getBillPdfResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(),
            "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_PDF_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");
    }

    @Test
    public void test_userGetStornoBillPdf() throws Exception {
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //Set Storno
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, false));
        ticketService.storno(bill.getId(), false);

        //get request
        MvcResult getStornoBillPdfResult = this.mockMvc
            .perform(get(TICKET_BASE_URI + "/" + bill.getId() + "/stornoBillPdf")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = getStornoBillPdfResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus(),
            "Response: " + response.getContentAsString());
        assertEquals(MediaType.APPLICATION_PDF_VALUE, response.getContentType());
        response.setCharacterEncoding("utf-8");
    }

    @Test
    public void test_userGetOrderPdfWrongId_notFound() throws Exception {
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //Set Storno
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, false));
        ticketService.storno(bill.getId(), false);

        long wrongId = 1000000L;

        //get request
        MvcResult mvcResult = this.mockMvc
            .perform(get(TICKET_BASE_URI + "/" + wrongId + "/stornoBillPdf")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus(),
            "Response: " + response.getContentAsString());
    }

    @Test
    public void test_userGetBillPdfWrongId_notFound() throws Exception {
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //Set Storno
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, false));
        ticketService.storno(bill.getId(), false);

        long wrongId = 1000000L;

        //get request
        MvcResult mvcResult = this.mockMvc.perform(get(TICKET_BASE_URI + "/" + wrongId + "/billPdf")
            .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus(),
            "Response: " + response.getContentAsString());
    }

    @Test
    public void test_userGetStornoBillPdfWrongId_notFound() throws Exception {
        Show show = saveLocationHallplanEventShow();

        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        seats.put("R1", list);
        Booked booked = new Booked(seats, new HashMap<>(), show);

        //register&login "mark23@gmail.com": "meGood12*"
        ApplicationUser user = TestData.buildApplicationUser();
        String token = login(user);

        //Set Storno
        Bill bill = billRepository.save(BillMapper.bookedToBill(booked, show, user, false));
        ticketService.storno(bill.getId(), false);

        long wrongId = 1000000L;

        //get request
        MvcResult mvcResult = this.mockMvc
            .perform(get(TICKET_BASE_URI + "/" + wrongId + "/stornoBillPdf")
                .header(securityProperties.getAuthHeader(), token))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus(),
            "Response: " + response.getContentAsString());
    }
}
