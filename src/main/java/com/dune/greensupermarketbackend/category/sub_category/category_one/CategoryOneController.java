package com.dune.greensupermarketbackend.category.sub_category.category_one;

import com.dune.greensupermarketbackend.ApiVersionConfig;

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

import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneResponseMessageDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.service.CategoryOneService;

import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/l1-category")
@AllArgsConstructor
public class CategoryOneController {

    private CategoryOneService categoryOneService;

    // Get the list of all L1 sub categories
    @GetMapping("all-categories")
    public ResponseEntity<List<CategoryOneDto>> getAllCategories() {
        List<CategoryOneDto> list = categoryOneService.getAllCategories();
        return ResponseEntity.ok(list);
    }

    // Get the L1 sub category by ID
    @GetMapping("{subCatOneId}")
    public ResponseEntity<CategoryOneDto> getCategoryById(@PathVariable("subCatOneId") Integer subCatOneId) {
        CategoryOneDto categoryOneDto = categoryOneService.getCategoryById(subCatOneId);
        return new ResponseEntity<>(categoryOneDto, HttpStatus.OK);
    }

    // Add a new L1 sub category
    @PostMapping("add-category")
    public ResponseEntity<CategoryOneResponseMessageDto> addCategory(@RequestBody CategoryOneDto categoryOneDto) {
        CategoryOneResponseMessageDto categoryOneResponseMessageDto = categoryOneService.addCategory(categoryOneDto);
        return new ResponseEntity<>(categoryOneResponseMessageDto, HttpStatus.CREATED);
    }

    // Update a L1 sub category
    @PutMapping("update-category/{subCatOneId}")
    public ResponseEntity<CategoryOneResponseMessageDto> updateCategory(
            @PathVariable("subCatOneId") Integer subCatOneId,
            @RequestBody CategoryOneDto updateCategory) {
        CategoryOneResponseMessageDto categoryOneResponseMessageDto = categoryOneService.updateCategory(subCatOneId,
                updateCategory);
        return new ResponseEntity<>(categoryOneResponseMessageDto, HttpStatus.CREATED);
    }

    // Delete a L1 sub category
    @DeleteMapping("delete-category/{subCatOneId}")
    public ResponseEntity<CategoryOneResponseMessageDto> deleteCategory(
            @PathVariable("subCatOneId") Integer subCatOneId) {
        CategoryOneResponseMessageDto categoryOneResponseMessageDto = categoryOneService.deleteCategory(subCatOneId);
        return new ResponseEntity<>(categoryOneResponseMessageDto, HttpStatus.OK);
    }

    @GetMapping("main-category/{main-id}")
    public ResponseEntity<List<CategoryOneDto>> getByMainCategory(@PathVariable("main-id") Integer mainCategoryId){
        List<CategoryOneDto> categoryOneDtos = categoryOneService.getAllByMainCategory(mainCategoryId);
        return ResponseEntity.ok(categoryOneDtos);
    }
}
