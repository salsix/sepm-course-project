package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

public class LocationDto {
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

    @Transient
    private Set<HallplanSimpleDto> hallplans;

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

    public Set<HallplanSimpleDto> getHallplans() {
        return hallplans;
    }

    public void setHallplans(Set<HallplanSimpleDto> hallplans) {
        this.hallplans = hallplans;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
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
        LocationDto that = (LocationDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(country, that.country) &&
            Objects.equals(city, that.city) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(street, that.street) &&
            Objects.equals(hallplans, that.hallplans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, city, zip, street, hallplans);
    }
}
