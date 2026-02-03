package com.asem.ucar.Image.service;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> getDirectUploadSignature() {

        long timestamp = System.currentTimeMillis() / 1000L;

        Map<String, Object> paramsToSign = new TreeMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", "AdvertisementImages");
        paramsToSign.put("allowed_formats", "jpg,png,webp");

        String signature = cloudinary.apiSignRequest(
                paramsToSign,
                cloudinary.config.apiSecret,
                2 // signature version
        );

        Map<String, Object> response = new HashMap<>();
        response.put("api_key", cloudinary.config.apiKey);
        response.put("timestamp", timestamp);
        response.put("signature", signature);
        response.put("folder", "AdvertisementImages");

        response.put("maxFileSize", 7_000_000); // 7MB
        response.put("allowedFormats", "jpg,png,webp");

        return response;
    }
}