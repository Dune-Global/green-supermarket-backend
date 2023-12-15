package com.dune.greensupermarketbackend.brand;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brand")
public class BrandEntity {
    @Id
    @Column(name = "brand_id", nullable = false)
    private Integer brandId;

    @Column(name = "brand_name", nullable = false)
    private String brandName;
}
