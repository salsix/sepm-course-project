package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booked;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopTen;
import java.util.List;

public interface TicketService {
    /**
     * Gives back bill by id.
     *
     * @param billId id of bill
     * @return bill by id
     */
    Bill getById(Long billId);

    /**
     * Gives back bought and reserved tickets for shows in the future.
     *
     * @return bought and reserved tickets for shows in the future
     */
    List<Bill> getUserBills();

    /**
     * Gives back history of bought tickets for shows in the past and also cancelled bought tickets for shows in the past and future.
     *
     * @param page shown page in pagination
     * @return history of bought tickets for shows in the past and also cancelled tickets for shows in the past and future
     */
    List<Bill> getPastUserBills(int page);

    /**
     * Gives back total price of cancelled tickets, if they were only reserved 0.
     *
     * @param billId id of bill
     * @param onlyReserved true if reserved tickets are cancelled
     * @return price of cancelled tickets if they have been bought, otherwise if only reserved 0
     */
    double storno(Long billId, boolean onlyReserved);

    /**
     * Gives back all booked seats/area tickets.
     *
     * @param showId id of show
     * @return all booked seats/area tickets of show
     */
    Booked getAllBooked(Long showId);

    /**
     * Gives back price of booked tickets.
     *
     * @param booked the chosen seats/area tickets
     * @param reserved true if tickets are to be reserved
     * @return price of booked tickets
     */
    Bill bookNew(Booked booked, boolean reserved);

    /**
     * Gives back price of booked tickes.
     *
     * @param billId id of bill
     * @param booked the chosen seats/area tickets
     * @return price of booked tickes
     */
    Long buyReserved(Long billId, Booked booked);

    /**
     * Gives back the list of top 10 events according to category.
     *
     * @param category category of event
     * @return list of top 10 events according to category
     */
    List<TopTen> topTen(String category);

    /**
     * Saves a Qr-Code for the OrderPdf.
     *
     * @param billId id of the bill where a Qr-Code should be created
     * @return stringFormat of the qrImage
     */
    String orderQr(Long billId);
}
