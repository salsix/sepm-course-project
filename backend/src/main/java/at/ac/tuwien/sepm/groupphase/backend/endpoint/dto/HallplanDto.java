package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HallplanDto {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private List<HallplanCatDto> cats;
    @NotNull
    private Set<HallplanSeatDto> seats;
    @NotNull
    private Set<HallplanAreaDto> areas;
    private LocationSimpleDto location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HallplanCatDto> getCats() {
        return cats;
    }

    public void setCats(List<HallplanCatDto> cats) {
        this.cats = cats;
    }

    public Set<HallplanSeatDto> getSeats() {
        return seats;
    }

    public void setSeats(Set<HallplanSeatDto> seats) {
        this.seats = seats;
    }

    public Set<HallplanAreaDto> getAreas() {
        return areas;
    }

    public void setAreas(Set<HallplanAreaDto> areas) {
        this.areas = areas;
    }

    public LocationSimpleDto getLocation() {
        return location;
    }

    public void setLocation(LocationSimpleDto location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "HallplanDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", cats=" + cats +
            ", seats=" + seats +
            ", areas=" + areas +
            ", location=" + location +
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
        HallplanDto that = (HallplanDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(cats, that.cats) &&
            Objects.equals(seats, that.seats) &&
            Objects.equals(areas, that.areas) &&
            Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cats, seats, areas, location);
    }
}
