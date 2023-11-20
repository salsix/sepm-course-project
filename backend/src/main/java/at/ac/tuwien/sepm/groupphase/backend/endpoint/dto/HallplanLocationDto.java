package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class HallplanLocationDto {

    private Long id;
    @NotBlank
    private String name;

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

    public LocationSimpleDto getLocation() {
        return location;
    }

    public void setLocation(LocationSimpleDto location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "HallplanLocationDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", location=" + location +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallplanLocationDto that = (HallplanLocationDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}
