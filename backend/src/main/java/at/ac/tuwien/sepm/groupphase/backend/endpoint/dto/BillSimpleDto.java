package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BillSimpleDto {
    private Long id;

    /**price before tax.*/
    private double price;

    private LocalDateTime buyTime;
    private LocalDateTime stornoTime;

    @NotNull
    private Boolean reserved;
    @NotNull
    private Boolean storno;

    ShowSimpleDto show;

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

    public ShowSimpleDto getShow() {
        return show;
    }

    public void setShow(ShowSimpleDto show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "BillSimpleDto{" +
            "id=" + id +
            ", price=" + price +
            ", buyTime=" + buyTime +
            ", stornoTime=" + stornoTime +
            ", reserved=" + reserved +
            ", storno=" + storno +
            ", show=" + show +
            '}';
    }
}
