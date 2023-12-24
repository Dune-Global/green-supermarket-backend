package com.dune.greensupermarketbackend.category.sub_category.category_two;

import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;

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

    @Override
    public String toString() {
        return "CategoryTwoEntity{" +
                "subCatTwoId=" + subCatTwoId +
                ", subCatTwoName='" + subCatTwoName + '\'' +
                ", subCatTwoDescription='" + subCatTwoDescription + '\'' +
                '}';
    }
}