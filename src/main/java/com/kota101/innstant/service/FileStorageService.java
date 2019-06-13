package com.kota101.innstant.service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    JSONObject store(MultipartFile file, String saveDirectory) throws JSONException;

    Resource loadAsResource(String fileName);
}
