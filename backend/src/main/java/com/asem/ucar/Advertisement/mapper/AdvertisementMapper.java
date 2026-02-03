package com.asem.ucar.Advertisement.mapper;

import com.asem.ucar.Advertisement.dto.AdvertisementRequest;
import com.asem.ucar.Advertisement.dto.AdvertisementResponse;
import com.asem.ucar.Advertisement.dto.AdvertisementUpdateRequest;
import com.asem.ucar.Advertisement.model.Advertisement;
import com.asem.ucar.Image.model.Image;
import com.asem.ucar.User.model.User;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "images", ignore = true)
    Advertisement toEntity(AdvertisementRequest request, User owner);

    @Mapping(target = "ownerName", expression = "java(ad.getOwner().getUsername())")
    @Mapping(target = "ownerPhoneNumber", expression = "java(ad.getOwner().getPhoneNumber())")
    @Mapping(target = "imageUrls", source = "images")
    AdvertisementResponse toResponse(Advertisement ad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntityFromRequest(AdvertisementUpdateRequest request, @MappingTarget Advertisement ad);

    default List<String> mapImagesToUrls(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream()
                .map(Image::getUrl)
                .toList();
    }
}

