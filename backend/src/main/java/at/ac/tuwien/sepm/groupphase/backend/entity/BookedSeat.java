package at.ac.tuwien.sepm.groupphase.backend.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class BookedSeat {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String identifier;
    @NotNull
    private Integer seat;

    @NotNull
    private boolean storno = false;

    @ManyToOne(fetch = FetchType.EAGER)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bill bill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isStorno() {
        return storno;
    }

    public void setStorno(boolean storno) {
        this.storno = storno;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }


    @Override
    public String toString() {
        return "BookedSeat{" +
            "id=" + id +
            ", identifier='" + identifier + '\'' +
            ", seat=" + seat +
            ", storno=" + storno +
            ", show=" + show +
            ", bill=" + bill +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookedSeat that = (BookedSeat) o;
        return storno == that.storno &&
            Objects.equals(id, that.id) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(seat, that.seat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, seat, storno);
    }
}
