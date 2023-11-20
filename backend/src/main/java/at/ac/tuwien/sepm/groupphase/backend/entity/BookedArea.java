package at.ac.tuwien.sepm.groupphase.backend.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class BookedArea {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String identifier;
    @NotNull @Min(1)
    private Integer count;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isStorno() {
        return storno;
    }

    public void setStorno(boolean storno) {
        this.storno = storno;
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
        return "BookedArea{" +
            "id=" + id +
            ", identifier='" + identifier + '\'' +
            ", count=" + count +
            ", storno=" + storno +
            ", show=" + show +
            ", bill=" + bill +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookedArea area = (BookedArea) o;
        return storno == area.storno &&
            Objects.equals(id, area.id) &&
            Objects.equals(identifier, area.identifier) &&
            Objects.equals(count, area.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, count, storno);
    }
}
