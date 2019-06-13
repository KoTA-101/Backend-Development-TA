package com.kota101.innstant.properties;

import lombok.Getter;

import java.net.URI;
import java.nio.file.Paths;

@Getter
public class StorageProperties {
    private final URI BASE_IMG_DIRECTORY = Paths.get("").resolve("img").toAbsolutePath().toUri();
}
