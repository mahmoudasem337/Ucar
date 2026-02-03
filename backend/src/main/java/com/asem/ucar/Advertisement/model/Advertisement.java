package com.asem.ucar.Advertisement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.asem.ucar.Image.model.Image;
import com.asem.ucar.User.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "advertisement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private String ownerLocation;
    private String carDescription;
    private BigDecimal carPrice;
    private String carModel;
    private String carMake;
    private int carProductionYear;
    private String carBodyType;
    private String carFuelType;
    private String carTransmissionType;
    private String carColor;
    private BigDecimal engineCapacity;
    private BigDecimal kilometers;
    private LocalDateTime createdAt;
    @OneToMany(
            mappedBy = "advertisement",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Image> images = new ArrayList<>();
}

