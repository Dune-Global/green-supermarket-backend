package com.dune.greensupermarketbackend.category.main_category;

import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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


    @OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryOneEntity> categoryOnes;
}
