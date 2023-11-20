package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Booked {
    private Map<String, ArrayList<Integer>> seats;
    private Map<String, Integer> areas;

    private Show show;

    public Booked() {
    }

    public Booked(Map<String, ArrayList<Integer>> seats, Map<String, Integer> areas, Show show) {
        this.seats = seats;
        this.areas = areas;
        this.show = show;
    }

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

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "Booked{" +
            "seats=" + seats +
            ", areas=" + areas +
            ", show=" + show +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booked booked = (Booked) o;
        return Objects.equals(seats, booked.seats) &&
            Objects.equals(areas, booked.areas) &&
            Objects.equals(show, booked.show);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seats, areas, show);
    }
}
