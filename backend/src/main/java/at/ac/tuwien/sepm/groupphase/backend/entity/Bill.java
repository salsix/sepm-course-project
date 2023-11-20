package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class Bill {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * price before tax.
     */
    private double price;
    private int ticketCount;

    @PastOrPresent
    private LocalDateTime buyTime;
    @PastOrPresent
    private LocalDateTime stornoTime;

    @NotNull
    private Boolean reserved;
    @NotNull
    private Boolean storno;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bill")
    private Set<BookedSeat> seats;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bill")
    private Set<BookedArea> areas;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser applicationUser;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Show show;

    @Transient
    private Booked booked;

    public Bill() {
    }

    public Bill(Long id, double price, LocalDateTime buyTime,
                LocalDateTime stornoTime, @NotNull Boolean reserved,
                @NotNull Boolean storno,
                Set<BookedSeat> seats, Set<BookedArea> areas,
                ApplicationUser applicationUser,
                @NotNull Show show) {
        this.id = id;
        this.price = price;
        this.buyTime = buyTime;
        this.stornoTime = stornoTime;
        this.reserved = reserved;
        this.storno = storno;
        this.seats = seats;
        this.areas = areas;
        this.applicationUser = applicationUser;
        this.show = show;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double beforeTax) {
        this.price = beforeTax;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public LocalDateTime getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(LocalDateTime buyTime) {
        this.buyTime = buyTime;
    }

    public LocalDateTime getStornoTime() {
        return stornoTime;
    }

    public void setStornoTime(LocalDateTime stornoTime) {
        this.stornoTime = stornoTime;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Boolean getStorno() {
        return storno;
    }

    public void setStorno(Boolean storno) {
        this.storno = storno;
    }

    public Set<BookedSeat> getSeats() {
        return seats;
    }

    public void setSeats(Set<BookedSeat> seats) {
        this.seats = seats;
    }

    public Set<BookedArea> getAreas() {
        return areas;
    }

    public void setAreas(Set<BookedArea> areas) {
        this.areas = areas;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Booked getBooked() {
        return booked;
    }

    public void setBooked(Booked booked) {
        this.booked = booked;
    }

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + id +
            ", price=" + price +
            ", ticketCount=" + ticketCount +
            ", buyTime=" + buyTime +
            ", stornoTime=" + stornoTime +
            ", reserved=" + reserved +
            ", storno=" + storno +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Double.compare(bill.getPrice(), getPrice()) == 0
            && getTicketCount() == bill.getTicketCount()
            && Objects.equals(getId(), bill.getId())
            && Objects.equals(getBuyTime(), bill.getBuyTime())
            && Objects.equals(getStornoTime(), bill.getStornoTime())
            && Objects.equals(getReserved(), bill.getReserved())
            && Objects.equals(getStorno(), bill.getStorno());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getTicketCount(), getBuyTime(), getStornoTime(), getReserved(), getStorno());
    }
}
