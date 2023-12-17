package com.dune.greensupermarketbackend.category.sub_category.category_one;

import com.dune.greensupermarketbackend.category.main_category.MainCategoryEntity;

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
@Table(name = "sub_category_one")
public class CategoryOneEntity {
    @Id
    @Column(name = "sub_one_id")
    private Integer subCatOneId;

    @Column(name = "sub_one_name", nullable = false)
    private String subCatOneName;

    @Column(name = "sub_one_des", nullable = false)
    private String subCatOneDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id", nullable = false)
    private MainCategoryEntity mainCategory;
}
