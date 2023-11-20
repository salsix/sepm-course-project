package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;

import java.util.List;

public interface LocationService {
    /**
     * returns all saved locations.
     *
     * @return all saved locations
     */
    List<Location> getAll();

    /**
     * get location by id.
     *
     * @param id location id
     * @return location
     */
    Location getById(Long id);

    /**
     * list of countries in which there are locations.
     *
     * @return list of countries in which there are locations
     */
    List<String> getCountries();

    /**
     * list of locations for country.
     *
     * @param country country to search locations in
     * @return list of locations for country
     */
    List<String> getCountryZips(String country);

    /**
     * get list of Locations for country and zip.
     *
     * @param country country to search locations in
     * @param zip zip to search locations in
     * @return list of locations for country and zip
     */
    List<Location> getCountryZipLocations(String country, String zip);

    /**
     * Save location.
     *
     * @param location location to save
     * @return location with saved id
     */
    Location save(Location location);

    /**
     * Edits location data.
     *
     * @param location location to edit
     */
    void edit(Location location);

    /**
     * deletes location according to id.
     *
     * @param id location id
     */
    void delete(Long id);
}
