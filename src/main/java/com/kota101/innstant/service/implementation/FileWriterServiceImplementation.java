package com.kota101.innstant.service.implementation;

import com.kota101.innstant.service.FileWriterService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileWriterServiceImplementation implements FileWriterService {
    private Path path = Paths.get("").resolve("innstant_map.wkt").toAbsolutePath();

    @Override
    public void writeToFile(String stringToWrite) throws IOException {
        initFile();
        Files.write(path, ("GEOMETRY_ID\tROOM_ID\tROOM_NAME\tOWNER_ID\tROOM_TYPE\tLOCATION\tDESCRIPTION\tRATING\tTOTAL_REVIEW\tWKT\n").getBytes());
        Files.write(path, stringToWrite.getBytes(), StandardOpenOption.APPEND);
    }

    @Override
    public void initFile() throws IOException {
        if (Files.notExists(path)) {
            Files.createFile(path);
        } else {
            FileChannel.open(path, StandardOpenOption.WRITE).truncate(0).close();
        }
    }
}
