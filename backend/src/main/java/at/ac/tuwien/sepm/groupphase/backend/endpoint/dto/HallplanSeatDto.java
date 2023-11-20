package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class HallplanSeatDto extends HallplanDataDto {

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

    public void setOffsetX(Double offsetX) {
        this.offsetX = offsetX;
    }

    public Double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Double offsetY) {
        this.offsetY = offsetY;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
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
        HallplanSeatDto that = (HallplanSeatDto) o;
        return Objects.equals(image, that.image) &&
            Objects.equals(positionX, that.positionX) &&
            Objects.equals(positionY, that.positionY) &&
            Objects.equals(offsetX, that.offsetX) &&
            Objects.equals(offsetY, that.offsetY) &&
            Objects.equals(startNumber, that.startNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, positionX, positionY, offsetX, offsetY, startNumber);
    }
}
