package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookedDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowFullDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.*;

@Mapper
public interface BillMapper {
    static HashMap<String, ArrayList<Integer>> seatListToIntegerMap(Iterable<BookedSeat> bookedSeats) {
        HashMap<String, ArrayList<Integer>> seats = new HashMap<>();
        for (BookedSeat s : bookedSeats) {
            String id = s.getIdentifier();
            ArrayList<Integer> list;

            if (!seats.containsKey(id)) {
                list = new ArrayList<>();
                seats.put(id, list);
            } else {
                list = seats.get(id);
            }

            list.add(s.getSeat());
        }
        return seats;
    }

    static HashMap<String, Integer> areaListToIntegerMap(Iterable<BookedArea> bookedAreas) {
        HashMap<String, Integer> areas = new HashMap<>();
        for (BookedArea a : bookedAreas) {
            String id = a.getIdentifier();
            if (a.getCount() == null) {
                throw new ValidationException("Area " + id + " has null count");
            }

            if (!areas.containsKey(id)) {
                areas.put(id, a.getCount());
            } else {
                areas.put(id, areas.get(id) + a.getCount());
            }
        }
        return areas;
    }

    static Bill bookedToBill(Booked booked, Show show, ApplicationUser user, boolean reserved) {
        return bookedToBill(booked, show, user, reserved, LocalDateTime.now());
    }

    static Bill bookedToBill(Booked booked, Show show, ApplicationUser user, boolean reserved, LocalDateTime date) {
        //make new bill
        Bill bill = new Bill(null, 0, date, null, reserved, false, null, null, user, show);
        int count = 0;

        HashSet<BookedSeat> wantSeats = new HashSet<>();
        for (String id : booked.getSeats().keySet()) {
            for (Integer seatNumber : booked.getSeats().get(id)) {
                BookedSeat s = new BookedSeat();
                s.setIdentifier(id);
                s.setShow(show);
                s.setSeat(seatNumber);
                s.setBill(bill);

                wantSeats.add(s);
                ++count;
            }
        }

        HashSet<BookedArea> wantAreas = new HashSet<>();
        for (String id : booked.getAreas().keySet()) {
            BookedArea a = new BookedArea();
            a.setIdentifier(id);
            a.setShow(show);
            a.setCount(booked.getAreas().get(id));
            a.setBill(bill);
            wantAreas.add(a);

            count += a.getCount();
        }

        bill.setSeats(wantSeats);
        bill.setAreas(wantAreas);
        bill.setTicketCount(count);

        return bill;
    }

    default BillDto entityToDto(Bill bill) {
        //make new booked for want
        BookedDto wantBookedDto = new BookedDto();
        wantBookedDto.setSeats(seatListToIntegerMap(bill.getSeats()));
        wantBookedDto.setAreas(areaListToIntegerMap(bill.getAreas()));

        //return
        return new BillDto(bill.getId(), bill.getPrice(), bill.getBuyTime(),
            bill.getReserved(), bill.getStorno(), wantBookedDto, bookedEntityToDto(bill.getBooked()),
            showEntityToDto(bill.getShow()));
    }

    List<BillSimpleDto> entityToSimpleDto(List<Bill> bills);

    ShowFullDto showEntityToDto(Show show);

    BookedDto bookedEntityToDto(Booked booked);
}
