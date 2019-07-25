package com.kota101.innstant.service;

import java.io.IOException;

public interface FileWriterService {
    void writeToFile(String stringToWrite) throws IOException;

    void initFile() throws IOException;
}
