package com.dune.greensupermarketbackend.category.main_category;

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
@Table(name = "main_category")
public class MainCategoryEntity {
    @Id
    @Column(name = "main_category_id")
    private Integer mainCategoryId;

    @Column(name = "main_category_name", nullable = false)
    private String mainCategoryName;

    @Column(name = "main_category_description", nullable = false)
    private String mainCategoryDesc;
}
