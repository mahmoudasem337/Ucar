package com.asem.ucar.Advertisement.service;

import java.util.List;
import com.asem.ucar.Advertisement.dto.AdvertisementRequest;
import com.asem.ucar.Advertisement.dto.AdvertisementResponse;
import com.asem.ucar.Advertisement.dto.AdvertisementUpdateRequest;
import com.asem.ucar.Advertisement.exception.AdvertisementNotFoundException;
import com.asem.ucar.Advertisement.mapper.AdvertisementMapper;
import com.asem.ucar.Advertisement.model.Advertisement;
import com.asem.ucar.Advertisement.repository.AdvertisementRepository;
import com.asem.ucar.Auth.service.AuthService;
import com.asem.ucar.Image.model.Image;
import com.asem.ucar.Image.service.CloudinaryService;
import com.asem.ucar.User.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.asem.ucar.Advertisement.exception.AccessDeniedException.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final AuthService authService;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public AdvertisementResponse createAdvertisement(AdvertisementRequest request) {

        User currentUser = authService.getCurrentUserEntity();
        Advertisement ad = advertisementMapper.toEntity(request, currentUser);
        if (request.images() != null && !request.images().isEmpty()) {
            List<Image> images = request.images().stream()
                    .map(url -> Image.builder()
                            .url(url)
                            .advertisement(ad)
                            .build())
                    .toList();
            ad.setImages(images);
        }
        Advertisement saved = advertisementRepository.save(ad);
        return advertisementMapper.toResponse(saved);
    }

    public void deleteAdvertisement(Long id) {

        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(AdvertisementNotFoundException::new);

        User currentUser = authService.getCurrentUserEntity();

        if (!ad.getOwner().getId().equals(currentUser.getId())) {
            throw AccessDeniedException();
        }

        advertisementRepository.delete(ad);
    }

    public List<AdvertisementResponse> findAll() {
        return advertisementRepository.findAll()
                .stream()
                .map(advertisementMapper::toResponse)
                .toList();
    }

    public AdvertisementResponse getById(Long id) {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(AdvertisementNotFoundException::new);

        return advertisementMapper.toResponse(ad);
    }

    @Transactional
    public AdvertisementResponse updateAdvertisement(AdvertisementUpdateRequest request) {

        Advertisement ad = advertisementRepository.findById(request.advertisementId())
                .orElseThrow(AdvertisementNotFoundException::new);

        User currentUser = authService.getCurrentUserEntity();

        if (!ad.getOwner().getId().equals(currentUser.getId())) {
            throw AccessDeniedException();
        }

        advertisementMapper.updateEntityFromRequest(request, ad);

        if (request.images() != null && !request.images().isEmpty()) {
            List<Image> images = request.images().stream()
                    .map(url -> Image.builder()
                            .url(url)
                            .advertisement(ad)
                            .build())
                    .toList();
            ad.getImages().clear();
            ad.getImages().addAll(images);
        }
        Advertisement saved = advertisementRepository.save(ad);
        return advertisementMapper.toResponse(saved);
    }
}
