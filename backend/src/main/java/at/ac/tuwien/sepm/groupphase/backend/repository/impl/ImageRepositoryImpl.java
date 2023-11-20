package at.ac.tuwien.sepm.groupphase.backend.repository.impl;

import at.ac.tuwien.sepm.groupphase.backend.config.DocumentStorageProperty;
import at.ac.tuwien.sepm.groupphase.backend.exception.ImageFileExeption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    private static final long MAX_SIZE = (long) 3e6;

    private final Path basePath;

    @Autowired
    public ImageRepositoryImpl(DocumentStorageProperty documentStorageConfig) throws IOException {
        basePath = Paths.get(documentStorageConfig.getUploadDirectory()).toAbsolutePath()
            .normalize();
        initDirectory();
    }

    private void initDirectory() throws IOException {
        if (Files.notExists(basePath)) {
            Files.createDirectory(basePath);
        }
    }

    @Override
    public void save(String name, byte[] image) {
        Path path = basePath.resolve(name);
        if (image.length > MAX_SIZE) {
            throw new ImageFileExeption("Cannot save image with size>" + MAX_SIZE);
        }
        try {
            Files.write(path, image);
        } catch (Exception e) {
            throw new ImageFileExeption("Could not save image");
        }
    }

    @Override
    public byte[] get(String name) {
        Path path = basePath.resolve(name);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new NotFoundException("Could not load image");
        }
    }

    @Override
    public void delete(String name) {
        Path path = basePath.resolve(name);

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new ImageFileExeption("Could not delete image");
        }
    }

    @Override
    public void deleteAll() {

        String dir = basePath.toString();
        try {
            FileUtils.deleteDirectory(new File(dir));
        } catch (IOException e) {
            throw new ImageFileExeption("Could not delete all images");
        }
        try {
            initDirectory();
        } catch (IOException e) {
            throw new ImageFileExeption("Could not delete all images, internal error");
        }
    }
}
