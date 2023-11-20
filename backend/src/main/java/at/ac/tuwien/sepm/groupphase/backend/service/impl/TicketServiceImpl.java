package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booked;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedArea;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedSeat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanArea;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanCat;
import at.ac.tuwien.sepm.groupphase.backend.entity.HallplanSeat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopTen;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedAreaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedSeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.util.QrcodeUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final int stornoReserveBefore;
    private final UserRepository userRepository;

    private final ShowRepository showRepository;

    private final BillRepository billRepository;
    private final BookedSeatRepository bookedSeatRepository;
    private final BookedAreaRepository bookedAreaRepository;

    public TicketServiceImpl(UserRepository userRepository,
                             ShowRepository showRepository,
                             BillRepository billRepository,
                             BookedSeatRepository bookedSeatRepository,
                             BookedAreaRepository bookedAreaRepository,
                             @Value("${application-config.storno-reserved-minutes: 30}") int storno) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.billRepository = billRepository;
        this.bookedSeatRepository = bookedSeatRepository;
        this.bookedAreaRepository = bookedAreaRepository;
        stornoReserveBefore = storno;
    }

    private static void hallplanToHashmaps(Hallplan hallplan,
                                           HashMap<String, ArrayList<HallplanSeat>> hallSeats,
                                           HashMap<String, HallplanArea> hallAreas) {
        for (HallplanSeat s : hallplan.getSeats()) {
            String id = s.getIdentifier() + s.getRowNumber();

            ArrayList<HallplanSeat> list;
            if (!hallSeats.containsKey(id)) {
                list = new ArrayList<>();
                hallSeats.put(id, list);
            } else {
                list = hallSeats.get(id);
            }

            list.add(s);
        }
        for (HallplanArea a : hallplan.getAreas()) {
            hallAreas.put(a.getIdentifier() + a.getRowNumber(), a);
        }
    }

    private static HallplanSeat getHallplanSeat(ArrayList<HallplanSeat> list, Integer seat) {
        for (HallplanSeat hs : list) {
            //seat in between start and start+count: start <= seat < start+count
            if (hs.getStartNumber() <= seat && seat < hs.getStartNumber() + hs.getCount()) {
                return hs;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Bill getById(Long billId) {
        LOGGER.trace("getById({})", billId);
        Bill bill = billRepository.findByIdEager(billId)
            .orElseThrow(() -> new NotFoundException("Bill does not exist"));

        //only allow loading bill from logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new NotFoundException("Not logged in"));
        if (!bill.getApplicationUser().getId().equals(user.getId())) {
            throw new NotFoundException("Bill does not exist");
        }

        //get all booked
        Booked booked = getAllBooked(bill.getShow().getId());
        bill.setBooked(booked);

        //remove want from booked
        Map<String, ArrayList<Integer>> seats = booked.getSeats();
        for (BookedSeat s : bill.getSeats()) {
            String id = s.getIdentifier();
            if (seats.containsKey(id)) {
                ArrayList<Integer> list = seats.get(id);
                int index = list.indexOf(s.getSeat());
                if (index != -1) {
                    list.remove(index);
                }
            }
        }
        Map<String, Integer> areas = booked.getAreas();
        for (BookedArea a : bill.getAreas()) {
            String id = a.getIdentifier();
            if (areas.containsKey(id)) {
                Integer count = areas.get(id) - a.getCount();
                areas.put(id, count);
            }
        }

        return bill;
    }

    @Override
    @Transactional
    public List<Bill> getUserBills() {
        LOGGER.trace("getUserBills()");

        //get current logged in info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new NotFoundException("Not logged in"));

        LocalDateTime now = LocalDateTime.now();
        return billRepository.findActiveByFutureOrPresentAndApplicationUserEager(user, LocalDate.now(), now.getHour(), now.getMinute());
    }

    @Override
    public List<Bill> getPastUserBills(int page) {
        LOGGER.trace("getPastUserBills({})", page);

        //get current logged in info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new NotFoundException("Not logged in"));

        Pageable pageable = PageRequest.of(page, 5);

        LocalDateTime now = LocalDateTime.now();
        return billRepository.findByPastAndApplicationUserEager(user, LocalDate.now(), now.getHour(), now.getMinute(), pageable);
    }

    @Override
    public double storno(Long billId, boolean onlyReserved) {
        LOGGER.trace("storno({},{})", billId, onlyReserved);

        //already checks right user
        Bill bill = getById(billId);

        if (bill.getStorno()) {
            throw new ConflictException("Tickets already cancelled!");
        }
        if (onlyReserved && !bill.getReserved()) {
            throw new ConflictException("Tickets already bought");
        }

        //check can storno (>30m before show)
        Show show = bill.getShow();

        //check date before
        //timezones - need something other than LocalDate in show
        LocalDate date = show.getDate();
        LocalDate now = LocalDate.now();
        if (now.compareTo(date) > 0) {
            //show in past
            throw new ConflictException("Cannot storno bill of show that already happened");
        }

        //same date
        if (now.compareTo(date) == 0) {
            //check hour, minutes
            LocalDateTime localDateTime = LocalDateTime.now();

            double time = localDateTime.getHour() + localDateTime.getMinute() / 60.0;
            //have to storno a certain time before show
            time += stornoReserveBefore / 60.0;

            double showTime = show.getHour() + show.getMinute() / 60.0;

            //show already started / in past
            if (time > showTime) {
                throw new ConflictException("Cannot storno bill of show that already happened");
            }
        }

        if (!bill.getReserved()) {
            //save storno
            bill.setStorno(true);
            bill.setStornoTime(LocalDateTime.now());
            for (BookedSeat b : bill.getSeats()) b.setStorno(true);
            for (BookedArea b : bill.getAreas()) b.setStorno(true);
            billRepository.save(bill);
        } else {
            //delete bill since it was reserved
            billRepository.delete(bill);
        }

        //return refunded money
        if (!bill.getReserved()) {
            return bill.getPrice();
        }
        return 0;
    }

    @Override
    @Transactional
    public Booked getAllBooked(Long showId) {
        LOGGER.trace("getAllBooked({})", showId);
        Show show = showRepository.findByIdEagerHallplan(showId).orElseThrow(() -> new NotFoundException("Show does not exist"));

        Booked booked = new Booked();
        booked.setShow(show);

        List<BookedSeat> bookedSeats = bookedSeatRepository.findActiveByShowId(showId);
        List<BookedArea> bookedAreas = bookedAreaRepository.findActiveByShowId(showId);

        booked.setSeats(BillMapper.seatListToIntegerMap(bookedSeats));
        booked.setAreas(BillMapper.areaListToIntegerMap(bookedAreas));
        return booked;
    }

    @Override
    @Transactional
    public Bill bookNew(Booked booked, boolean reserved) {
        LOGGER.trace("bookNew({},{})", booked, reserved);

        //check has any tickets
        if (booked.getSeats().size() == 0 && booked.getAreas().size() == 0) {
            throw new ValidationException("No booked seats or areas");
        }
        //check show id set
        if (booked.getShow() == null || booked.getShow().getId() == null) {
            throw new ValidationException("Show not set");
        }
        Long showId = booked.getShow().getId();

        //get show, hallplan
        Show show = showRepository.findByIdEagerHallplan(showId).orElseThrow(() -> new NotFoundException("Show does not exist"));
        Hallplan hallplan = show.getHallplan();
        HallplanCat[] cats = new HallplanCat[hallplan.getCats().size()];
        hallplan.getCats().toArray(cats);

        //save seats and areas in hashmap for later checks if seat/area exists
        HashMap<String, ArrayList<HallplanSeat>> hallSeats = new HashMap<>();
        HashMap<String, HallplanArea> hallAreas = new HashMap<>();
        hallplanToHashmaps(hallplan, hallSeats, hallAreas);

        //get current logged in info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new NotFoundException("Not logged in");
        }
        ApplicationUser user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new NotFoundException("Not logged in"));

        //make new bill
        Bill bill = BillMapper.bookedToBill(booked, show, user, reserved);
        double price = 0;

        //get already booked seats
        List<BookedSeat> savedSeatList = bookedSeatRepository.findActiveByShowIdAndIdentifier(showId, booked.getSeats().keySet());
        HashMap<String, ArrayList<Integer>> savedSeats = BillMapper.seatListToIntegerMap(savedSeatList);

        //check valid seats and not yet booked, increment price
        for (BookedSeat s : bill.getSeats()) {
            //check row exists in hallplan
            if (s.getSeat() == null || !hallSeats.containsKey(s.getIdentifier())) {
                throw new ValidationException("Seat doesn't exist: " + s.getIdentifier() + ":" + s.getSeat());
            }

            //check if seat number exists, increment price if exists
            HallplanSeat hs = getHallplanSeat(hallSeats.get(s.getIdentifier()), s.getSeat());
            if (hs == null) {
                throw new ValidationException("Seat doesn't exist: " + s.getIdentifier() + ":" + s.getSeat());
            }
            price += cats[hs.getCat()].getPrice();

            //check not already booked
            if (!savedSeats.containsKey(s.getIdentifier())) {
                continue;
            }
            if (savedSeats.get(s.getIdentifier()).contains(s.getSeat())) {
                throw new ConflictException("Seat already booked (" + s.getIdentifier() + ":" + s.getSeat() + ")");
            }
        }

        //get already booked area counts
        List<BookedArea> savedAreaList = bookedAreaRepository.findActiveByShowIdAndIdentifier(showId, booked.getAreas().keySet());
        HashMap<String, Integer> savedAreas = BillMapper.areaListToIntegerMap(savedAreaList);

        //check wantAreas valid, not too many booked, increment price
        for (BookedArea area : bill.getAreas()) {
            String id = area.getIdentifier();

            //want 0
            if (area.getCount() <= 0) {
                throw new ValidationException("Cannot book empty area: " + id);
            }

            //check area exists in hallplan
            if (!hallAreas.containsKey(id)) {
                throw new ValidationException("Area doesn't exist: " + id);
            }

            //get count
            if (area.getCount() == null) {
                throw new ValidationException("Area " + id + " no count set");
            }
            Integer count = area.getCount();
            if (savedAreas.containsKey(id)) {
                count += savedAreas.get(id);
            }

            //check booked count <= max count
            HallplanArea ha = hallAreas.get(id);
            if (count > ha.getCount()) {
                throw new ConflictException("Cannot book more seats than available in area (" + id + ")");
            }

            //increment price
            price += cats[ha.getCat()].getPrice() * area.getCount();
        }

        //save bill
        bill.setPrice(price);
        billRepository.save(bill);

        return bill;
    }

    @Override
    @Transactional
    public Long buyReserved(Long billId, Booked booked) {
        LOGGER.trace("buyReserved({},{})", billId, booked);

        this.storno(billId, true);
        return this.bookNew(booked, false).getId();
    }

    @Override
    public List<TopTen> topTen(String category) {
        Pageable pageable = PageRequest.of(0, 10);

        //get 1 month before
        LocalDateTime date = LocalDateTime.now().minusMonths(1);

        //query
        List<Tuple> tuples = billRepository.topTen(category, date, pageable);

        //make tuples into TopTen objects
        List<TopTen> topTens = new ArrayList<>();
        long firstCount = 0;
        for (Tuple t : tuples) {
            Long count = (Long) t.get(1);
            if (firstCount == 0) {
                firstCount = count;
            }
            topTens.add(new TopTen((Event) t.get(0), 100 * count / firstCount));
        }
        return topTens;
    }

    @Override
    public String orderQr(Long billId) {

        final ByteArrayOutputStream qrOutput = new ByteArrayOutputStream();
        BufferedImage bufferedImage = QrcodeUtil
            .generateQrCodeImage("https://localhost:4200/checkTicket/" + billId.toString());
        try {
            ImageIO.write(bufferedImage, "jpg", qrOutput);
            return Base64.getEncoder().encodeToString(qrOutput.toByteArray());

        } catch (IOException ioException) {
            throw new RuntimeException("Could not create QR-image");
        }
    }
}
