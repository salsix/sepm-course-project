package at.ac.tuwien.sepm.groupphase.backend.repository;

public interface ImageRepository {
    void save(String name, byte[] image);

    byte[] get(String name);

    void delete(String name);

    void deleteAll();
}
