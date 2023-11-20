package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class HallplanArea extends HallplanData {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderColumn
    private List<HallplanPosition> positions;

    public List<HallplanPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<HallplanPosition> positions) {
        this.positions = positions;
    }

    public HallplanArea() {
    }

    public HallplanArea(Long id,
                        Integer count,
                        String identifier,
                        Integer rowNumber,
                        Integer cat,
                        List<HallplanPosition> positions) {
        super(id, count, identifier, rowNumber, cat);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HallplanArea that = (HallplanArea) o;
        return Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), positions);
    }
}
