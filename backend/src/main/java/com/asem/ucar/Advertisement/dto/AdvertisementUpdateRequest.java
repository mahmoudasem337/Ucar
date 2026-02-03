package com.asem.ucar.Advertisement.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record AdvertisementUpdateRequest(

        @NotNull(message = "Advertisement ID is required")
        Long advertisementId,

        @NotBlank(message = "Owner location is required")
        @Size(max = 100, message = "Location must not exceed 100 characters")
        String ownerLocation,

        @NotBlank(message = "Car description is required")
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String carDescription,

        @NotNull(message = "Car price is required")
        @Positive(message = "Car price must be positive")
        BigDecimal carPrice,

        @NotBlank(message = "Car model is required")
        @Size(max = 50)
        String carModel,

        @NotBlank(message = "Car make is required")
        @Size(max = 50)
        String carMake,

        @Min(value = 1950, message = "Production year must be valid")
        @Max(value = 2100, message = "Production year must be valid")
        int carProductionYear,

        @NotBlank(message = "Car body type is required")
        String carBodyType,

        @NotBlank(message = "Car fuel type is required")
        String carFuelType,

        @NotBlank(message = "Car transmission type is required")
        String carTransmissionType,

        @NotBlank(message = "Car color is required")
        String carColor,

        @NotNull(message = "Engine capacity is required")
        @Positive(message = "Engine capacity must be positive")
        BigDecimal engineCapacity,

        @NotNull(message = "Kilometers is required")
        @PositiveOrZero(message = "Kilometers must be zero or positive")
        BigDecimal kilometers,

        @Size(max = 5, message = "You can upload up to 5 images only")
        List<String> images
) {
        public List<String> images() {
                return images == null ? new ArrayList<>() : images;
        }
}
