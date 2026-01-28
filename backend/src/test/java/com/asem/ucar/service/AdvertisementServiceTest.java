package com.asem.ucar.service;

import com.asem.ucar.Advertisement.dto.AdvertisementRequest;
import com.asem.ucar.Advertisement.dto.AdvertisementResponse;
import com.asem.ucar.Advertisement.dto.AdvertisementUpdateRequest;
import com.asem.ucar.Advertisement.exception.AccessDeniedException;
import com.asem.ucar.Advertisement.exception.AdvertisementNotFoundException;
import com.asem.ucar.Advertisement.mapper.AdvertisementMapper;
import com.asem.ucar.Advertisement.model.Advertisement;
import com.asem.ucar.Advertisement.repository.AdvertisementRepository;
import com.asem.ucar.Advertisement.service.AdvertisementService;
import com.asem.ucar.Auth.service.AuthService;
import com.asem.ucar.Image.service.CloudinaryService;
import com.asem.ucar.User.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Advertisement Service unit tests")
class AdvertisementServiceTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private AdvertisementMapper advertisementMapper;

    @Mock
    private AuthService authService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private AdvertisementService advertisementService;

    private User owner;
    private User otherUser;
    private Advertisement advertisement;
    private Advertisement advertisement2;


    @BeforeEach
    void setup() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("ahmed");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("mohamed");

        advertisement = new Advertisement();
        advertisement.setId(10L);
        advertisement.setOwner(owner);
        advertisement.setImages(new ArrayList<>());

        advertisement2 = new Advertisement();
        advertisement2.setId(11L);
        advertisement2.setOwner(owner);
        advertisement2.setImages(new ArrayList<>());
    }

    @Nested
    class CreateAdvertisementTests {

        @Test
        void shouldCreateAdvertisementSuccessfully() {
            //not used logically in the service, just passed to the mapper so mock it.
            AdvertisementRequest request = mock(AdvertisementRequest.class);
            AdvertisementResponse response = mock(AdvertisementResponse.class);

            when(authService.getCurrentUserEntity()).thenReturn(owner);
            when(advertisementMapper.toEntity(request, owner))
                    .thenReturn(advertisement);
            when(advertisementRepository.save(advertisement))
                    .thenReturn(advertisement);
            when(advertisementMapper.toResponse(advertisement))
                    .thenReturn(response);

            AdvertisementResponse result =
                    advertisementService.createAdvertisement(request);

            assertNotNull(result);

            verify(authService).getCurrentUserEntity();
            verify(advertisementMapper).toEntity(request, owner);
            verify(advertisementRepository).save(advertisement);
            verify(advertisementMapper).toResponse(advertisement);
        }
    }

    @Nested
    class DeleteAdvertisementTests {

        @Test
        void shouldDeleteAdvertisementWhenOwnerMatches() {

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.of(advertisement));
            when(authService.getCurrentUserEntity())
                    .thenReturn(owner);

            advertisementService.deleteAdvertisement(10L);

            verify(advertisementRepository).delete(advertisement);
        }

        @Test
        void shouldThrowAdvertisementNotFoundWhenAdvertisementDoesNotExist() {

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.empty());

            assertThrows(AdvertisementNotFoundException.class,
                    () -> advertisementService.deleteAdvertisement(10L));

            verify(advertisementRepository).findById(10L);
            verify(authService, never()).getCurrentUserEntity();
            verify(advertisementRepository, never()).delete(any());
        }

        @Test
        void shouldThrowAccessDeniedWhenUserIsNotOwner() {

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.of(advertisement));
            when(authService.getCurrentUserEntity())
                    .thenReturn(otherUser);

            assertThrows(AccessDeniedException.class,
                    () -> advertisementService.deleteAdvertisement(10L));

            verify(advertisementRepository).findById(10L);
            verify(advertisementRepository, never()).delete(any());
        }
    }

    @Nested
    class FindAllAdvertisementsTests {

        @Test
        void shouldReturnAllAdvertisements() {

            List<Advertisement> ads =
                    List.of(advertisement, advertisement2);

            when(advertisementRepository.findAll())
                    .thenReturn(ads);

            when(advertisementMapper.toResponse(any(Advertisement.class)))
                    .thenReturn(mock(AdvertisementResponse.class));

            List<AdvertisementResponse> result =
                    advertisementService.findAll();

            assertEquals(2, result.size());

            verify(advertisementRepository).findAll();
            verify(advertisementMapper, times(2))
                    .toResponse(any(Advertisement.class));
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldReturnAdvertisementWhenIdExists() {

            AdvertisementResponse response =
                    mock(AdvertisementResponse.class);

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.of(advertisement));
            when(advertisementMapper.toResponse(advertisement))
                    .thenReturn(response);

            AdvertisementResponse result =
                    advertisementService.getById(10L);

            assertNotNull(result);
            verify(advertisementRepository).findById(10L);
            verify(advertisementMapper).toResponse(advertisement);
        }

        @Test
        void shouldThrowAdvertisementNotFoundWhenIdDoesNotExist() {

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.empty());

            assertThrows(AdvertisementNotFoundException.class,
                    () -> advertisementService.getById(10L));

            verify(advertisementRepository).findById(10L);
        }
    }

    @Nested
    class UpdateAdvertisementTests {

        @Test
        void shouldUpdateAdvertisementSuccessfully() {

            AdvertisementUpdateRequest request =
                    mock(AdvertisementUpdateRequest.class);
            AdvertisementResponse response =
                    mock(AdvertisementResponse.class);

            when(request.advertisementId()).thenReturn(10L);
            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.of(advertisement));
            when(authService.getCurrentUserEntity())
                    .thenReturn(owner);
            when(advertisementRepository.save(advertisement))
                    .thenReturn(advertisement);
            when(advertisementMapper.toResponse(advertisement))
                    .thenReturn(response);

            AdvertisementResponse result =
                    advertisementService.updateAdvertisement(request);

            assertNotNull(result);

            verify(advertisementMapper)
                    .updateEntityFromRequest(request, advertisement);
            verify(advertisementRepository).save(advertisement);
            verify(advertisementMapper).toResponse(advertisement);
        }

        @Test
        void shouldThrowAdvertisementNotFoundWhenUpdatingNonExistingAdvertisement() {

            AdvertisementUpdateRequest request =
                    mock(AdvertisementUpdateRequest.class);
            when(request.advertisementId()).thenReturn(10L);

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.empty());

            assertThrows(AdvertisementNotFoundException.class,
                    () -> advertisementService.updateAdvertisement(request));

            verify(advertisementRepository).findById(10L);
        }

        @Test
        void shouldThrowAccessDeniedWhenUpdatingByNonOwner() {

            AdvertisementUpdateRequest request =
                    mock(AdvertisementUpdateRequest.class);
            when(request.advertisementId()).thenReturn(10L);

            when(advertisementRepository.findById(10L))
                    .thenReturn(Optional.of(advertisement));
            when(authService.getCurrentUserEntity())
                    .thenReturn(otherUser);

            assertThrows(AccessDeniedException.class,
                    () -> advertisementService.updateAdvertisement(request));

            verify(advertisementRepository).findById(10L);
            verify(advertisementRepository, never()).save(any());
        }
    }
}
