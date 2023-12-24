package com.dune.greensupermarketbackend.category.sub_category.category_two;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoResponseMessageDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.service.CategoryTwoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/l2-category")
@AllArgsConstructor
public class CategoryTwoController {
    private CategoryTwoService categoryTwoService;

    // Get the list of all L2 sub categories
    @GetMapping("all-categories")
    public ResponseEntity<List<CategoryTwoDto>> getAllCategories() {
        List<CategoryTwoDto> list = categoryTwoService.getAllCategories();
        return ResponseEntity.ok(list);
    }

    // Get the L2 sub category by ID
    @GetMapping("{subCatTwoId}")
    public ResponseEntity<CategoryTwoDto> getCategoryById(@PathVariable("subCatTwoId") Integer subCatTwoId) {
        CategoryTwoDto categoryTwoDto = categoryTwoService.getCategoryById(subCatTwoId);
        return new ResponseEntity<>(categoryTwoDto, HttpStatus.OK);
    }

    // Add a new L2 sub category
    @PostMapping("add-category")
    public ResponseEntity<CategoryTwoResponseMessageDto> addCategory(@RequestBody CategoryTwoDto categoryTwoDto) {
        CategoryTwoResponseMessageDto categoryTwoResponseMessageDto = categoryTwoService.addCategory(categoryTwoDto);
        return new ResponseEntity<>(categoryTwoResponseMessageDto, HttpStatus.CREATED);
    }

    // Update a L2 sub category
    @PutMapping("update-category/{subCatTwoId}")
    public ResponseEntity<CategoryTwoResponseMessageDto> updateCategory(
            @PathVariable("subCatTwoId") Integer subCatTwoId,
            @RequestBody CategoryTwoDto updateCategory) {
        CategoryTwoResponseMessageDto categoryTwoResponseMessageDto = categoryTwoService.updateCategory(subCatTwoId,
                updateCategory);
        return new ResponseEntity<>(categoryTwoResponseMessageDto, HttpStatus.CREATED);
    }

    // Delete a L2 sub category
    @DeleteMapping("delete-category/{subCatTwoId}")
    public ResponseEntity<CategoryTwoResponseMessageDto> deleteCategory(
            @PathVariable("subCatTwoId") Integer subCatTwoId) {
        CategoryTwoResponseMessageDto categoryTwoResponseMessageDto = categoryTwoService.deleteCategory(subCatTwoId);
        return new ResponseEntity<>(categoryTwoResponseMessageDto, HttpStatus.OK);
    }

    //Get By Sub category one id
    @GetMapping("subone-category/{subCatOneId}")
    public ResponseEntity<List<CategoryTwoDto>> getBySubCategoryOneId(@PathVariable("subCatOneId") Integer subCatOneId){
        List<CategoryTwoDto> categoryTwoDtos = categoryTwoService.getAllBySubCatOne(subCatOneId);
        return ResponseEntity.ok(categoryTwoDtos);
    }
}
