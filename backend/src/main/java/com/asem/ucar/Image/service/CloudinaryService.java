package com.asem.ucar.Image.service;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(String file) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "AdvertisementImages");
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getDirectUploadSignature(String folder) {
        long timestamp = System.currentTimeMillis() / 1000L;

        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        if (folder != null && !folder.isEmpty()) {
            paramsToSign.put("folder", folder);
        }

        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret, 1);

        Map<String, Object> response = new HashMap<>();
        response.put("api_key", cloudinary.config.apiKey);
        response.put("timestamp", timestamp);
        response.put("signature", signature);
        if (folder != null && !folder.isEmpty()) {
            response.put("folder", folder);
        }
        return response;
    }
}