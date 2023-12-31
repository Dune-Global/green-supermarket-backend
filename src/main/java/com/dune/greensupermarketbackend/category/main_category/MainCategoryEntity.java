package com.dune.greensupermarketbackend.category.main_category;

import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.product.ProductEntity;
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

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "main_category_description", nullable = false)
    private String mainCategoryDesc;

    private String imgUrl;

    @OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryOneEntity> categoryOnes;

    @OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;

    @Override
    public String toString() {
        return "MainCategoryEntity{" +
                "mainCategoryId=" + mainCategoryId +
                ", mainCategoryName='" + mainCategoryName + '\'' +
                ", mainCategoryDesc='" + mainCategoryDesc + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
