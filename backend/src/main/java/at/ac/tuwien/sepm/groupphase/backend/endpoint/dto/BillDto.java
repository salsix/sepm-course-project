package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

//dont copy everything from Bill - Show, BookedDto instead of BookedArea/BookedSeat lists.
public class BillDto {
    BookedDto want;
    BookedDto booked;
    ShowFullDto show;
    private Long id;
    /**
     * price before tax.
     */
    private double price;
    private LocalDateTime buyTime;
    private LocalDateTime stornoTime;

    @NotNull
    private Boolean reserved;
    @NotNull
    private Boolean storno;

    public BillDto() {
    }

    public BillDto(Long id, double price, LocalDateTime buyTime,
                   @NotNull Boolean reserved, @NotNull Boolean storno,
                   BookedDto want, BookedDto booked, ShowFullDto show) {
        this.id = id;
        this.price = price;
        this.buyTime = buyTime;
        this.reserved = reserved;
        this.storno = storno;
        this.want = want;
        this.booked = booked;
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

    public void setPrice(double price) {
        this.price = price;
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

    public BookedDto getWant() {
        return want;
    }

    public void setWant(BookedDto want) {
        this.want = want;
    }

    public BookedDto getBooked() {
        return booked;
    }

    public void setBooked(BookedDto booked) {
        this.booked = booked;
    }

    public ShowFullDto getShow() {
        return show;
    }

    public void setShow(ShowFullDto show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "BillDto{" +
            "want=" + want +
            ", booked=" + booked +
            ", show=" + show +
            ", id=" + id +
            ", price=" + price +
            ", buyTime=" + buyTime +
            ", stornoTime=" + stornoTime +
            ", reserved=" + reserved +
            ", storno=" + storno +
            '}';
    }
}
