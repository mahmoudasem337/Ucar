package com.asem.ucar.Advertisement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AdvertisementResponse(

        String ownerName,
        String ownerLocation,
        String ownerPhoneNumber,
        String carDescription,
        BigDecimal carPrice,
        String carModel,
        String carMake,
        int carProductionYear,
        String carBodyType,
        String carFuelType,
        String carTransmissionType,
        String carColor,
        BigDecimal engineCapacity,
        BigDecimal kilometers,
        LocalDateTime createdAt,
        List<String> imageUrls
) {}