package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class HallplanPosition {
    @Id
    @GeneratedValue
    private Long id;

    private Double x;
    private Double y;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double positionX) {
        this.x = positionX;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double positionY) {
        this.y = positionY;
    }

    public HallplanPosition() {
    }

    public HallplanPosition(Long id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "HallplanPosition{" +
            "id=" + id +
            ", positionX=" + x +
            ", positionY=" + y +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallplanPosition that = (HallplanPosition) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(x, that.x) &&
            Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y);
    }
}
