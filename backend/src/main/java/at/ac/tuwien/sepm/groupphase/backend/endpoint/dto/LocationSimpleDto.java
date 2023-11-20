package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class LocationSimpleDto {
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


    @Override
    public String toString() {
        return "LocationSimpleDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", zip='" + zip + '\'' +
            ", street='" + street + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationSimpleDto that = (LocationSimpleDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(country, that.country) &&
            Objects.equals(city, that.city) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, city, zip, street);
    }
}
