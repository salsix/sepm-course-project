package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
public class Location {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String zip;
    @NotBlank
    private String street;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "location")
    @NotNull
    private Set<Hallplan> hallplans;

    public Location() {
    }

    public Location(Long id, @NotBlank String name,
                    @NotBlank String country, @NotBlank String city, @NotBlank String zip,
                    @NotBlank String street,
                    @NotNull Set<Hallplan> hallplans) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.zip = zip;
        this.street = street;
        this.hallplans = hallplans;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Set<Hallplan> getHallplans() {
        return hallplans;
    }

    public void setHallplans(Set<Hallplan> hallplans) {
        this.hallplans = hallplans;
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", zip='" + zip + '\'' +
            ", street='" + street + '\'' +
            ", hallplans=" + hallplans +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) &&
            Objects.equals(name, location.name) &&
            Objects.equals(country, location.country) &&
            Objects.equals(city, location.city) &&
            Objects.equals(zip, location.zip) &&
            Objects.equals(street, location.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, city, zip, street);
    }
}
