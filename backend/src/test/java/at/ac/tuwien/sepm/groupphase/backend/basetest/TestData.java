package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booked;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanArea;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanCat;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanPosition;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanSeat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.springframework.mock.web.MockMultipartFile;

public interface TestData {

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String BASE_URI = "/api/v1";

    String USER_BASE_URI = BASE_URI + "/users";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

    //Location/////////////////////////////////////////////////////////////////////////////////////////////
    String LOCATION_BASE_URI = BASE_URI + "/locations";

    static Location buildLocation() {
        return new Location(null,
            "Haydn Kino",
            "Österreich",
            "Wien",
            "1070",
            "Mariahilferstraße 5a/1", new HashSet<>());
    }

    static List<Location> buildLocationList() {
        //dont change data
        ArrayList<Location> list = new ArrayList<>();
        Location location = new Location(null,
            "Haydn Kino",
            "Österreich",
            "Wien",
            "1070",
            "Mariahilferstraße 5a/1", new HashSet<>());
        location.getHallplans().add(buildHallplan("Saal A"));
        location.getHallplans().add(buildHallplan("Saal B"));
        location.getHallplans().add(buildHallplan("Saal C"));
        list.add(location);

        list.add(new Location(null,
            "Haydn Kino 2",
            "Österreich",
            "Wien",
            "1070",
            "Mariahilferstraße 7/1", new HashSet<>()));

        list.add(new Location(null,
            "Staatsoper",
            "Österreich",
            "Wien",
            "1010",
            "Gürtel 7/1", new HashSet<>()));

        list.add(new Location(null,
            "Was weiß ich",
            "Deutschland",
            "Wien",
            "1070 13",
            "Berlinerstraße 7/1", new HashSet<>()));
        return list;
    }

    //ApplicationUser//////////////////////////////////////////////////////////////////////////////////////////////
    String REGISTRATION_BASE_URI = BASE_URI + "/registration";

    static ApplicationUser buildApplicationUser() {
        return new ApplicationUser(null, "Mark", "Ger", "mark23@gmail.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, 0, false);
    }

    static ApplicationUser buildApplicationUser2() {
        return new ApplicationUser(null, "Markus", "Ger", "newEmail@gmail.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, 0, false);
    }

    static ApplicationUser buildApplicationUser3() {
        return new ApplicationUser(null, "Mark", "Ger", "mark23@gmaill.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, 0, false);
    }

    static ApplicationUser buildApplicationUser4() {
        return new ApplicationUser(null, "Mark", "Ger", "mark@gmaill.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, 0, false);
    }

    static ApplicationUserDto buildApplicationUser1() {
        return new ApplicationUserDto(null, "Markus", "Ger", "mark23@gmail.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, 0, false);
    }

    static ApplicationUser buildApplicationUserLocked() {
        return new ApplicationUser(null, "Markus", "Ger", "markLocked@gmail.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, true, 0, false);
    }

    static ApplicationUser buildApplicationUserMaxLoginFails(int maxLoginFails) {
        return new ApplicationUser(null, "Markus", "Ger", "markLoginFail@gmail.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, maxLoginFails, false);
    }

    static ApplicationUser buildApplicationUserPasswordReset() {
        return new ApplicationUser(null, "Markus", "Ger", "markReset@gmail.com", "meGood12*", "Male", Date.valueOf("1997-05-05"), false, false, 0, false);
    }

    //Hallplan/////////////////////////////////////////////////////////////////////////////////////////////
    String HALLPLAN_BASE_URI = BASE_URI + "/hallplans";

    static Hallplan buildHallplan() {
        return new Hallplan(null, "Saal A", null,
            new ArrayList<>(), new HashSet<>(), new HashSet<>());
    }

    static Hallplan buildHallplan(String name) {
        return new Hallplan(null, name, null,
            new ArrayList<>(), new HashSet<>(), new HashSet<>());
    }

    static Hallplan buildHallplanValid() {
        Hallplan hallplan = new Hallplan(null, "Saal A", null,
            new ArrayList<>(), null, null);
        ArrayList<HallplanCat> cats = new ArrayList<>();
        HashSet<HallplanSeat> seats = new HashSet<>();
        HashSet<HallplanArea> areas = new HashSet<>();

        cats.add(new HallplanCat(null, 10.0, "#ff0000"));
        cats.add(new HallplanCat(null, 20.0, "#ffff00"));
        seats.add(new HallplanSeat(null, 10, "R", 1, 0,
            "seat1.png", 10.0, 50.0,
            14.0, 0.0, 1));
        seats.add(new HallplanSeat(null, 10, "R", 1, 0,
            "seat1.png", 10.0, 100.0,
            14.0, 0.0, 11));

        areas.add(new HallplanArea(null, 100, "S", 1,
            0, new ArrayList<>()));
        areas.add(new HallplanArea(null, 60, "S", 2,
            1, new ArrayList<>()));

        hallplan.setCats(cats);
        hallplan.setSeats(seats);
        hallplan.setAreas(areas);
        return hallplan;
    }

    static Hallplan buildHallplanError_SeatsNotUnique() {
        Hallplan hallplan = new Hallplan(null, "Saal A", null,
            new ArrayList<>(), null, null);
        ArrayList<HallplanCat> cats = new ArrayList<>();
        HashSet<HallplanSeat> seats = new HashSet<>();
        HashSet<HallplanArea> areas = new HashSet<>();

        cats.add(new HallplanCat(null, 10.0, "#ff0000"));
        seats.add(new HallplanSeat(null, 10, "R", 1, 0,
            "seat1.png", 10.0, 20.0,
            14.0, 0.0, 1));
        seats.add(new HallplanSeat(null, 10, "R", 1, 0,
            "seat1.png", 10.0, 20.0,
            14.0, 0.0, 5)); //startNumber should be 11

        hallplan.setCats(cats);
        hallplan.setSeats(seats);
        hallplan.setAreas(areas);
        return hallplan;
    }

    static Hallplan buildHallplanError_SeatRowNotOneCat() {
        Hallplan hallplan = new Hallplan(null, "Saal A", null,
            new ArrayList<>(), null, null);
        ArrayList<HallplanCat> cats = new ArrayList<>();
        HashSet<HallplanSeat> seats = new HashSet<>();
        HashSet<HallplanArea> areas = new HashSet<>();

        cats.add(new HallplanCat(null, 10.0, "#ff0000"));
        cats.add(new HallplanCat(null, 20.0, "#ffff00"));
        seats.add(new HallplanSeat(null, 10, "R", 1, 0,
            "seat1.png", 10.0, 20.0,
            14.0, 0.0, 1));
        seats.add(new HallplanSeat(null, 10, "R", 1, 1,
            "seat1.png", 10.0, 20.0,
            14.0, 0.0, 11)); //cat must be same

        hallplan.setCats(cats);
        hallplan.setSeats(seats);
        hallplan.setAreas(areas);
        return hallplan;
    }

    static Hallplan buildHallplanError_SeatCatInvalid() {
        Hallplan hallplan = new Hallplan(null, "Saal A", null,
            new ArrayList<>(), null, null);
        ArrayList<HallplanCat> cats = new ArrayList<>();
        HashSet<HallplanSeat> seats = new HashSet<>();
        HashSet<HallplanArea> areas = new HashSet<>();

        cats.add(new HallplanCat(null, 10.0, "#ff0000"));
        seats.add(new HallplanSeat(null, 10, "R", 1, 1,
            "seat1.png", 10.0, 20.0,
            14.0, 0.0, 11)); //cat must be same

        hallplan.setCats(cats);
        hallplan.setSeats(seats);
        hallplan.setAreas(areas);
        return hallplan;
    }

    static Hallplan buildHallplanError_AreaNotUnique() {
        Hallplan hallplan = new Hallplan(null, "Saal A", null,
            new ArrayList<>(), null, null);
        ArrayList<HallplanCat> cats = new ArrayList<>();
        HashSet<HallplanSeat> seats = new HashSet<>();
        HashSet<HallplanArea> areas = new HashSet<>();

        cats.add(new HallplanCat(null, 10.0, "#ff0000"));
        areas.add(new HallplanArea(null, 100, "S", 1,
            0, new ArrayList<>()));
        areas.add(new HallplanArea(null, 50, "S", 1,
            0, new ArrayList<>()));

        hallplan.setCats(cats);
        hallplan.setSeats(seats);
        hallplan.setAreas(areas);
        return hallplan;
    }

    static Hallplan buildHallplanError_AreaCatInvalid() {
        Hallplan hallplan = new Hallplan(null, "Saal A", null,
            new ArrayList<>(), null, null);
        ArrayList<HallplanCat> cats = new ArrayList<>();
        HashSet<HallplanSeat> seats = new HashSet<>();
        HashSet<HallplanArea> areas = new HashSet<>();

        cats.add(new HallplanCat(null, 10.0, "#ff0000"));
        areas.add(new HallplanArea(null, 100, "S", 1,
            1, new ArrayList<>()));

        hallplan.setCats(cats);
        hallplan.setSeats(seats);
        hallplan.setAreas(areas);
        return hallplan;
    }

    //Cat
    static HallplanCat buildHallplanCat() {
        return new HallplanCat(null, 20.5, "#ff0000");
    }

    //Seat
    static HallplanSeat buildHallplanSeat() {
        return new HallplanSeat(null, 10, "R", 1, 0, "seat1.png", 10.0, 20.0,
            14.0, 0.0, 1);
    }

    //Area
    static HallplanArea buildHallplanArea() {
        return new HallplanArea(null, 100, "S", 1,
            0, new ArrayList<>());
    }

    //Position
    static HallplanPosition buildHallplanPosition() {
        return new HallplanPosition(null, 10.0, 20.0);
    }


    //Event/////////////////////////////////////////////////////////////////////////////////////////////
    String EVENT_BASE_URI = BASE_URI + "/events";

    static MockMultipartFile buildMockImage() {
        return new MockMultipartFile("image", "image.png", "image/png", new byte[]{0, 2, 3, 4});
    }

    static Event buildEvent() {
        return new Event(null, "Jumanji", 100, "cool event", "some artist", null, new HashSet<>());
    }

    static Event buildEvent(String name) {
        return new Event(null, name, 100, "cool event", "some artist", null, new HashSet<>());
    }

    static Event buildEvent(String name, EventCategory eventCategory) {
        return new Event(null, name, 100, "cool event", "some artist", eventCategory, new HashSet<>());
    }

    //News/////////////////////////////////////////////////////////////////////////////////////////////
    String NEWS_BASE_URI = BASE_URI + "/news";

    static MockMultipartFile[] buildMockMultiImages() {
        MockMultipartFile[] mockMultipartFile = new MockMultipartFile[2];
        mockMultipartFile[0] = new MockMultipartFile("image1", "image1.png", "images/png",
            new byte[]{0, 2, 3, 4});
        mockMultipartFile[1] = new MockMultipartFile("image2", "image2.png", "images/png",
            new byte[]{5, 6, 7, 8});

        return mockMultipartFile;
    }

    static MockMultipartFile buildMockImagesNews() {
        return new MockMultipartFile("images", "image.png", "image/png", new byte[]{0, 2, 3, 4});
    }

    static List<News> buildNewsList() {

        ArrayList<News> list = new ArrayList<>();

        list.set(0, buildNews("Neuer Film1"));
        list.set(1, buildNews("Neuer Film2"));
        return list;
    }

    static News buildNews() {
        List<ApplicationUser> list = new ArrayList<>();
        return new News(null, "NeuerFilm ist Da", "BlaBla", "ist seht gut", list, 2);
    }

    static News buildNews(String name) {
        List<ApplicationUser> list = new ArrayList<>();
        return new News(null, name, "BlaBla", "ist seht gut", list, 2);
    }

    static News buildNews(String name, Long id) {
        List<ApplicationUser> list = new ArrayList<>();
        return new News(id, name, "BlaBla", "ist seht gut", list, 2);
    }

    //Show/////////////////////////////////////////////////////////////////////////////////////////////
    String SHOW_BASE_URI = BASE_URI + "/shows";

    static Show buildShow() {
        return new Show(null, LocalDate.parse((LocalDate.now().getYear()+1)+"-05-02"), 14, 30, null, null);
    }

    static Show buildShow(LocalDate date) {
        return new Show(null, date, 14, 30, null, null);
    }


    //Ticket/////////////////////////////////////////////////////////////////////////////////////////////
    String TICKET_BASE_URI = BASE_URI + "/tickets";

    static Booked buildBooked(Show show){
        return buildBooked(show, 2, 2);
    }
    static Booked buildBooked(Show show, int seatCount, int areaCount){
        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        HashMap<String, Integer> areas = new HashMap<>();

        Hallplan hallplan = show.getHallplan();

        //take first seatCount tickets
        for(HallplanSeat s: hallplan.getSeats()) {
            String id = s.getIdentifier()+s.getRowNumber();
            for(int i = 0; i<s.getCount(); ++i){
                if(--seatCount<0){
                    break;
                }
                ArrayList<Integer> list;
                if(!seats.containsKey(id)){
                    list = new ArrayList<>();
                    seats.put(id, list);
                } else {
                    list = seats.get(id);
                }
                list.add(s.getStartNumber()+i);
            }
        }

        //take first areaCount tickets
        for(HallplanArea a: hallplan.getAreas()){
            if(areaCount<=0){
                break;
            }
            String id = a.getIdentifier()+a.getRowNumber();
            if(a.getCount()<areaCount){
                areas.put(id, a.getCount());
                areaCount -= a.getCount();
            } else {
                areas.put(id, areaCount);
            }
        }

        return new Booked(seats, areas, show);
    }

}
