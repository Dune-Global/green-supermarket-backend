package com.dune.greensupermarketbackend.category.sub_category.category_two;

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
@Table(name = "sub_category_two")
public class CategoryTwoEntity {
    @Id
    @Column(name = "sub_two_id")
    private Integer subCatTwoId;

    @Column(name = "sub_one_name", nullable = false)
    private String subCatTwoName;

    @Column(name = "sub_two_desc")
    private String subCatTwoDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_one_id", nullable = false)
    private CategoryOneEntity categoryOne;

    @OneToMany(mappedBy = "l2Category")
    private List<ProductEntity> products1;

    @Override
    public String toString() {
        return "CategoryTwoEntity{" +
                "subCatTwoId=" + subCatTwoId +
                ", subCatTwoName='" + subCatTwoName + '\'' +
                ", subCatTwoDescription='" + subCatTwoDescription + '\'' +
                '}';
    }
}