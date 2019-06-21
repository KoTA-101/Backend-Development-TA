package com.kota101.innstant.service.implementation;

import com.kota101.innstant.exception.StorageException;
import com.kota101.innstant.exception.StorageFileNotFoundException;
import com.kota101.innstant.properties.StorageProperties;
import com.kota101.innstant.service.FileStorageService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImplementation implements FileStorageService {
    private StorageProperties properties;
    private Path baseImgDirectory;

    public FileStorageServiceImplementation() {
        this.properties = new StorageProperties();
        this.baseImgDirectory = Paths.get(properties.getBASE_IMG_DIRECTORY());
    }

    @Override
    public JSONObject store(MultipartFile file, String saveDirectory) throws JSONException {
        baseImgDirectory = Paths.get(properties.getBASE_IMG_DIRECTORY());
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file: " + fileName);
            }
            if (fileName.contains("..")) {
                throw new StorageException("Cannot store file with relative path outside current directory: " + fileName);
            }
            try (InputStream inputStream = file.getInputStream()) {
                if (saveDirectory.equals("profile") || saveDirectory.equals("id_card") || saveDirectory.equals("user_with_id_card") || saveDirectory.equals("room")) {
                    UUID uuid = UUID.randomUUID();
                    fileName = saveDirectory + "-" + uuid + "." + file.getOriginalFilename().split("\\.")[1];
                    Files.copy(inputStream, this.baseImgDirectory.resolve(saveDirectory).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    throw new StorageException("Wrong save directory " + saveDirectory);
                }
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + fileName, e);
        }
        return new JSONObject().put("fileName", fileName);
    }

    @Override
    public Resource loadAsResource(String fileName) {
        baseImgDirectory = Paths.get(properties.getBASE_IMG_DIRECTORY());
        try {
            String saveDirectory = fileName.split("-")[0];
            if (saveDirectory.equals("profile") || saveDirectory.equals("id_card") || saveDirectory.equals("user_with_id_card") || saveDirectory.equals("room")) {
                Path file = baseImgDirectory.resolve(saveDirectory).resolve(fileName);
                Resource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new StorageFileNotFoundException("Could not read file: " + fileName);
                }
            } else {
                throw new StorageException("Wrong save directory " + saveDirectory);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
        }
    }
}
