package com.asem.ucar.Advertisement.controller;
import java.util.List;
import java.util.Optional;

import com.asem.ucar.Advertisement.dto.AdvertisementRequest;
import com.asem.ucar.Advertisement.dto.AdvertisementResponse;
import com.asem.ucar.Advertisement.dto.AdvertisementUpdateRequest;
import com.asem.ucar.Advertisement.service.AdvertisementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping
    public ResponseEntity<AdvertisementResponse> createAdvertisement(
            @Valid @RequestBody AdvertisementRequest request) {

        AdvertisementResponse response = advertisementService.createAdvertisement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementResponse>> getAllAdvertisements() {
        List<AdvertisementResponse> list = advertisementService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> getAdvertisement(@PathVariable Long id) {
        AdvertisementResponse ad = advertisementService.getById(id);
        return ResponseEntity.ok(ad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<AdvertisementResponse> updateAdvertisement(
            @Valid @RequestBody AdvertisementUpdateRequest advertisementUpdateRequest) {
        AdvertisementResponse updated = advertisementService.updateAdvertisement(advertisementUpdateRequest);
        return ResponseEntity.ok(updated);
    }
}
