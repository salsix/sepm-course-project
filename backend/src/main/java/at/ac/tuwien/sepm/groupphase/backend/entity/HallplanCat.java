package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class HallplanCat {
    @Id
    @GeneratedValue
    private Long id;

    private Double price;
    private String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public HallplanCat() {
    }

    public HallplanCat(Long id, Double price, String color) {
        this.id = id;
        this.price = price;
        this.color = color;
    }

    @Override
    public String toString() {
        return "HallplanCat{" +
            "id=" + id +
            ", price=" + price +
            ", color=" + color +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallplanCat that = (HallplanCat) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, color);
    }
}
