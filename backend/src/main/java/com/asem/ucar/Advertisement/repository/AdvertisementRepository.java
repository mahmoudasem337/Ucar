package com.asem.ucar.Advertisement.repository;

import com.asem.ucar.Advertisement.model.Advertisement;
import com.asem.ucar.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement,Long> {
    List<Advertisement> findAllByOwner(User owner);
}
