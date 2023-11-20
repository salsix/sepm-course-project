package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
public class Hallplan {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderColumn
    private List<HallplanCat> cats = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<HallplanSeat> seats = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<HallplanArea> areas = new HashSet<>();

    public Hallplan() {
    }

    public Hallplan(Long id, @NotBlank String name,
                    Location location, List<HallplanCat> cats,
                    Set<HallplanSeat> seats,
                    Set<HallplanArea> areas) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cats = cats;
        this.seats = seats;
        this.areas = areas;
    }

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

    public List<HallplanCat> getCats() {
        return cats;
    }

    public void setCats(List<HallplanCat> cats) {
        this.cats = cats;
    }

    public Set<HallplanSeat> getSeats() {
        return seats;
    }

    public void setSeats(Set<HallplanSeat> seats) {
        this.seats = seats;
    }

    public Set<HallplanArea> getAreas() {
        return areas;
    }

    public void setAreas(Set<HallplanArea> areas) {
        this.areas = areas;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Hallplan{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", location=" + location +
            ", cats=" + cats +
            ", seats=" + seats +
            ", areas=" + areas +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hallplan hallplan = (Hallplan) o;
        return Objects.equals(id, hallplan.id) &&
            Objects.equals(name, hallplan.name) &&
            Objects.equals(location, hallplan.location) &&
            Objects.equals(cats, hallplan.cats) &&
            Objects.equals(seats, hallplan.seats) &&
            Objects.equals(areas, hallplan.areas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, cats, seats, areas);
    }
}
