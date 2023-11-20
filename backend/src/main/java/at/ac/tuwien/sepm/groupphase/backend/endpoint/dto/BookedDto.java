package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class BookedDto {
    private Map<String, ArrayList<Integer>> seats;
    private Map<String, Integer> areas;

    private ShowFullDto show;

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

    public ShowFullDto getShow() {
        return show;
    }

    public void setShow(ShowFullDto show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "BookedDto{" +
            "seats=" + seats +
            ", areas=" + areas +
            ", show=" + show +
            '}';
    }
}
