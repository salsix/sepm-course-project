package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class HallplanCatDto {

    private Double price;
    private String color;

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

    @Override
    public String toString() {
        return "HallplanCatDto{" +
            "price=" + price +
            ", color='" + color + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HallplanCatDto that = (HallplanCatDto) o;
        return Objects.equals(price, that.price) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, color);
    }
}
