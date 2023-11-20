package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class HallplanSeat extends HallplanData {

    private String image;

    private Double positionX;

    private Double positionY;

    private Double offsetX;

    private Double offsetY;

    private Integer startNumber;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Double offsetDistance) {
        this.offsetX = offsetDistance;
    }

    public Double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Double offsetAngle) {
        this.offsetY = offsetAngle;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }

    public HallplanSeat() {
    }

    public HallplanSeat(Long id,
                        Integer count,
                        String identifier,
                        Integer rowNumber,
                        Integer cat,
                        String image,
                        Double positionX,
                        Double positionY,
                        Double offsetX,
                        Double offsetY,
                        Integer startNumber) {
        super(id, count, identifier, rowNumber, cat);
        this.image = image;
        this.positionX = positionX;
        this.positionY = positionY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.startNumber = startNumber;
    }

    @Override
    public String toString() {
        return "HallplanSeat{" +
            super.toString() +
            "image='" + image + '\'' +
            ", positionX=" + positionX +
            ", positionY=" + positionY +
            ", offsetDistance=" + offsetX +
            ", offsetAngle=" + offsetY +
            ", startNumber=" + startNumber +
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
        if (!super.equals(o)) {
            return false;
        }
        HallplanSeat that = (HallplanSeat) o;
        return Objects.equals(image, that.image) &&
            Objects.equals(positionX, that.positionX) &&
            Objects.equals(positionY, that.positionY) &&
            Objects.equals(offsetX, that.offsetX) &&
            Objects.equals(offsetY, that.offsetY) &&
            Objects.equals(startNumber, that.startNumber);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(super.hashCode(), image, positionX, positionY, offsetX, offsetY, startNumber);
    }
}
