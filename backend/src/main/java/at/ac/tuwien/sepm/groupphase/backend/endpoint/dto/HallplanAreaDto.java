package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

public class HallplanAreaDto extends HallplanDataDto {

    private List<HallplanPositionDto> positions;

    public List<HallplanPositionDto> getPositions() {
        return positions;
    }

    public void setPositions(List<HallplanPositionDto> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "HallplanArea{" +
            super.toString() +
            "positions=" + positions +
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
        HallplanAreaDto that = (HallplanAreaDto) o;
        return Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), positions);
    }
}
