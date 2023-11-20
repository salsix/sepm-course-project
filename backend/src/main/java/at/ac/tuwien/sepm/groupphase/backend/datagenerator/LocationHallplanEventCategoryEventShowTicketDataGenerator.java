package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class LocationHallplanEventCategoryEventShowTicketDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_LOCATIONS = 8;
    private static final int NUMBER_OF_HALLPLANS_PER_LOCATION = 2;
    private static final int NUMBER_OF_EVENTS = 50;
    private static final String[] EVENT_CATEGORIES = {"Movie", "Concert", "Sports", "Classical Music", "Musical/Show"};
    private static final int NUMBER_OF_SHOWS_PER_EVENT = 2;
    private static final String RESOURCES_PATH = "src/main/resources";
    private static final int NUMBER_OF_TICKETS_PER_SHOW = 3;

    private static final HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> LOCATION_DATA = new HashMap<>() {
        {
            put("Österreich", new HashMap<>() {
                {
                    put("Wien", new HashMap<>() {
                        {
                            put("1010", new HashMap<>() {
                                {
                                    put("Opernring 2", "Staatsoper");
                                    put("Stephansplatz 3", "Stephansdom");
                                }
                            });
                            put("1060", new HashMap<>() {
                                {
                                    put("Mariahilfer Straße 57", "Haydn Kino");
                                    put("Gumpendorfer Straße 63", "Apollo Kino");
                                }
                            });
                            put("1150", new HashMap<>() {
                                {
                                    put("Lugnerstraße 2a", "Lugner");
                                    put("Lugnerstraße 2b", "Lugner 2");
                                    put("Lugnerstraße 3", "Lugner 3");
                                }
                            });
                        }
                    });
                    put("Innsbruck", new HashMap<>() {
                        {
                            put("6020", new HashMap<>() {
                                {
                                    put("Innstraße 5", "Metropol Kino");
                                }
                            });
                        }
                    });
                }
            });
        }
    };

    private final LocationRepository locationRepository;
    private final HallplanRepository hallplanRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public LocationHallplanEventCategoryEventShowTicketDataGenerator(LocationRepository locationRepository, HallplanRepository hallplanRepository,
                                                                     EventCategoryRepository eventCategoryRepository, EventRepository eventRepository,
                                                                     ShowRepository showRepository, UserRepository userRepository,
                                                                     BillRepository billRepository, ImageRepository imageRepository) {
        this.locationRepository = locationRepository;
        this.hallplanRepository = hallplanRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventRepository = eventRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.imageRepository = imageRepository;
    }

    public void generateLocationsHallplansEventCategoriesEventsShowsTickets(List<ApplicationUser> users) {
        LOGGER.debug("generate: Locations, Hallplans, EventCategories, Events, Shows, Tickets");

        //delete all
        billRepository.deleteAll();
        showRepository.deleteAll();
        eventRepository.deleteAll();
        eventCategoryRepository.deleteAll();
        hallplanRepository.deleteAll();
        locationRepository.deleteAll();

        //generate locations
        List<Location> locations = generateLocations();

        //generate hallplans
        List<Hallplan> hallplans = generateHallplans(locations);

        //generate event categories
        List<EventCategory> eventCategories = generateEventCategories();

        //generate events
        List<Event> events = generateEvents(eventCategories);

        //generate shows
        List<Show> shows = generateShows(events, hallplans);

        //generate tickets
        List<Bill> tickets = generateTickets(shows, users);
    }

    private <K> K getAnyInSet(Set<K> set) {
        Iterator<K> itr = set.iterator();
        if (!itr.hasNext()) return null;
        return itr.next();
    }

    private <K> K getAnyInSet(Set<K> set, int index) {
        Iterator<K> itr = set.iterator();
        if (!itr.hasNext()) return null;
        --index;
        index %= set.size();
        while (--index > 0) {
            itr.next();
        }
        return itr.next();
    }

    private Location randLocation(String name) {
        String country = getAnyInSet(LOCATION_DATA.keySet());
        String city = getAnyInSet(LOCATION_DATA.get(country).keySet());
        String zip = getAnyInSet(LOCATION_DATA.get(country).get(city).keySet());
        String street = getAnyInSet(LOCATION_DATA.get(country).get(city).get(zip).keySet());
        return new Location(null, name, country, city, zip, street, new HashSet<>());
    }


    private List<Location> generateLocations() {
        List<Location> locations = new ArrayList<>();
        int count = 0;
        //generate from real data
        for (String country : LOCATION_DATA.keySet()) {
            for (String city : LOCATION_DATA.get(country).keySet()) {
                for (String zip : LOCATION_DATA.get(country).get(city).keySet()) {
                    for (String street : LOCATION_DATA.get(country).get(city).get(zip).keySet()) {
                        String name = LOCATION_DATA.get(country).get(city).get(zip).get(street);
                        locations.add(new Location(null, name, country, city, zip, street, new HashSet<>()));
                        if (++count > NUMBER_OF_LOCATIONS) {
                            break;
                        }
                    }
                    if (++count > NUMBER_OF_LOCATIONS) {
                        break;
                    }
                }
                if (++count > NUMBER_OF_LOCATIONS) {
                    break;
                }
            }
            if (++count > NUMBER_OF_LOCATIONS) {
                break;
            }
        }
        //generate random
        for (; count < NUMBER_OF_LOCATIONS; ++count) {
            locations.add(randLocation("Location " + count));
        }
        return locationRepository.saveAll(locations);
    }

    private List<Hallplan> generateHallplans(List<Location> locations) {
        List<Hallplan> hallplans = new ArrayList<>();
        for (Location location : locations) {
            for (int i = 1; i <= NUMBER_OF_HALLPLANS_PER_LOCATION; ++i) {
                ArrayList<HallplanCat> cats = new ArrayList<>();
                HashSet<HallplanSeat> seats = new HashSet<>();
                HashSet<HallplanArea> areas = new HashSet<>();

                int catCount = 4;
                cats.add(new HallplanCat(null, 10.0, "#ff0000"));
                cats.add(new HallplanCat(null, 15.0, "#0088ff"));
                cats.add(new HallplanCat(null, 20.0, "#7300ff"));
                cats.add(new HallplanCat(null, 25.0, "#1eff00"));

                int areaCount = 3;
                for (int a = 0; a < areaCount; ++a) {
                    ArrayList<HallplanPosition> positions = new ArrayList<>();
                    positions.add(new HallplanPosition(null, 50 + 200 * a + 0.0, 50.0));
                    positions.add(new HallplanPosition(null, 50 + 200 * a + 170.0, 50.0));
                    positions.add(new HallplanPosition(null, 50 + 200 * a + 170.0, 120.0));
                    positions.add(new HallplanPosition(null, 50 + 200 * a + 0.0, 120.0));

                    areas.add(new HallplanArea(null, 50 + 25 * a, "S", a + 1,
                        a % catCount, positions));
                }

                int seatCount = 8;
                for (int s = 0; s < seatCount; ++s) {
                    int category = (s / 2) % catCount;
                    seats.add(new HallplanSeat(null, 15, "R", s, category,
                        "seat2.png", 50.0, 150.0 + s * 25,
                        14.0, 0.0, 1));
                    seats.add(new HallplanSeat(null, 15, "R", s, category,
                        "seat2.png", 300.0, 150.0 + s * 25,
                        14.0, 0.0, 16));
                }

                seats.add(new HallplanSeat(null, 35, "A", 1, 0,
                    "seat2.png", 50.0, 150.0 + (seatCount + 1) * 25,
                    14.0, 0.0, 1));

                //add to list
                hallplans.add(new Hallplan(null, "Saal " + i, location,
                    cats, seats, areas));
            }
        }

        return hallplanRepository.saveAll(hallplans);
    }

    private List<EventCategory> generateEventCategories() {
        ArrayList<EventCategory> list = new ArrayList<>();
        for (String c : EVENT_CATEGORIES) {
            list.add(new EventCategory(c));
        }
        eventCategoryRepository.saveAll(list);
        return list;
    }

    private List<Event> generateEvents(List<EventCategory> eventCategories) {
        ArrayList<Event> list = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
            EventCategory eventCategory = eventCategories.get(i % eventCategories.size());
            list.add(new Event(null, "Jumanji " + eventCategory.getCategory() + " " + i, 150 + i * 10,
                "Jumanji is a 1995 American fantasy adventure film directed by Joe Johnston. " +
                    "It is loosely based on the 1981 children's book by Chris Van Allsburg and the first installment of the Jumanji franchise. ",
                "Van Allsburg, Greg Taylor, Jonathan Hensleigh, Jim Strain, Robin Williams, " +
                    "Kirsten Dunst, David Alan Grier, Bonnie Hunt, Jonathan Hyde, Bebe Neuwirth.",
                eventCategory, null
            ));
        }
        EventCategory eventCategory2 = new EventCategory("Concert");
        list.add(new Event(null, "Sommernachts-Konzert", 180,
            "Daniel Harding will conduct the Vienna Philharmonic Summer Night Concert in Schönbrunn for the " +
                "first time. The soloist will be Igor Levit. Under the motto \"Yearning for Distant Places\", " +
                "compositions by Bernstein, Verdi, Rachmaninov, Sibelius, Elgar, Debussy and Holst will be performed.",
            "Wiener Philharmoniker, Igor Levit, Daniel Harding",
            eventCategory2, null
        ));

        EventCategory eventCategory = new EventCategory("Movie");
        list.add(new Event(null, "Star Wars 8", 195,
            "Star Wars: The Last Jedi (also known as Star Wars: Episode VIII – " +
                "The Last Jedi) is a 2017 American epic space opera film written and directed by Rian Johnson. P" +
                "roduced by Lucasfilm and distributed by Walt Disney Studios Motion Pictures, it is the second insta" +
                "llment of the Star Wars sequel trilogy, following The Force Awakens (2015), and the eighth episode " +
                "of the nine-part \"Skywalker saga\". The film's ensemble cast includes Mark Hamill, Carrie Fisher," +
                " Adam Driver, Daisy Ridley, John Boyega, Oscar Isaac, Andy Serkis, Lupita Nyong'o, Domhnall Gleeso" +
                "n, Anthony Daniels, Gwendoline Christie, Kelly Marie Tran, Laura Dern, and Benicio del Toro. The " +
                "Last Jedi follows Rey as she seeks the aid of Luke Skywalker, in hopes of turning the tide" +
                " for the Resistance in the fight against Kylo Ren and the First Order, while General Leia Organa" +
                ", Finn, and Poe Dameron attempt to escape a First Order attack on the dwindling Resistance fl" +
                "eet. The film features the first posthumous film performance by Fisher, who died in December 2016, and the film is dedicated to her.",
            "Van Allsburg, Greg Taylor, Jonathan Hensleigh, Jim Strain, Robin Williams, " +
                "Kirsten Dunst, David Alan Grier, Bonnie Hunt, Jonathan Hyde, Bebe Neuwirth.",
            eventCategory, null
        ));

        List<Event> events = eventRepository.saveAll(list);

        try {
            Event event = events.get(events.size() - 1);
            byte[] bytes = Files.readAllBytes(Paths.get(RESOURCES_PATH + "/starwars-1.jpg"));
            imageRepository.save("event-" + event.getId(), bytes);
            event.setImage(true);
            eventRepository.save(event);
        } catch (IOException e) {
            throw new RuntimeException("Could not save images to news " + e);
        }

        return events;
    }

    private List<Show> generateShows(List<Event> events, List<Hallplan> hallplans) {
        ArrayList<Show> list = new ArrayList<>();
        int count = 0;
        int showCount = NUMBER_OF_EVENTS * NUMBER_OF_SHOWS_PER_EVENT;
        for (Event e : events) {
            for (int i = 0; i < NUMBER_OF_SHOWS_PER_EVENT; i++) {
                ++count;
                list.add(new Show(null, LocalDate.of(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(),
                    1 + (LocalDate.now().getDayOfMonth() + count) % 28),
                    count % 24, count % 60, e, hallplans.get(count % hallplans.size())));
            }
        }
        return showRepository.saveAll(list);
    }

    private List<Bill> generateTickets(List<Show> shows, List<ApplicationUser> users) {
        ApplicationUser admin = userRepository.findByEmail("admin@email.com")
            .orElseThrow(() -> new NotFoundException("admin does not exist"));

        List<Bill> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        for (Show show : shows) {
            for (int i = 0; i < NUMBER_OF_TICKETS_PER_SHOW; ++i) {
                ++count;
                Booked booked = new Booked(new HashMap<>(), new HashMap<>(), show);

                Hallplan hallplan = show.getHallplan();
                if (count % 4 != 0) {
                    //book 2 seats
                    HallplanSeat s = getAnyInSet(hallplan.getSeats(), count);

                    ArrayList<Integer> l = new ArrayList<>();
                    booked.getSeats().put(s.getIdentifier() + s.getRowNumber(), l);

                    int nr = (2 * count) % (s.getCount() - 1);
                    l.add(nr);
                    l.add(nr + 1);
                } else {
                    //book area
                    HallplanArea a = getAnyInSet(hallplan.getAreas(), count);
                    booked.getAreas().put(a.getIdentifier() + a.getRowNumber(), 2);

                    if (count % 8 == 0) {
                        booked.getAreas().put(a.getIdentifier() + a.getRowNumber(), 20 + count / 10);
                    }
                }

                Bill bill = BillMapper.bookedToBill(booked, show, i < 2 ? admin : users.get(count % users.size()), count % 3 == 0,
                    LocalDateTime.of(now.getYear(), now.getMonthValue() + (i < 2 ? -1 : 0), now.getDayOfMonth() / 2, 14, 30));
                bill.setPrice(40 + count / 2);
                list.add(bill);
            }
        }
        return billRepository.saveAll(list);
    }
}
