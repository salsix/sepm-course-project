package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookedSeatsDto {

    private Map<String, ArrayList<Integer>> seats = new HashMap<>();
    private Map<String, Integer> areas = new HashMap<>();

    public Map<String, ArrayList<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(Map<String, ArrayList<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, Integer> getAreas() {
        return areas;
    }

    public void setAreas(Map<String, Integer> areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "BookedSeatsDto{" +
            "seats=" + seats +
            ", areas=" + areas +
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
        BookedSeatsDto dto = (BookedSeatsDto) o;
        return Objects.equals(seats, dto.seats) &&
            Objects.equals(areas, dto.areas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seats, areas);
    }
}
