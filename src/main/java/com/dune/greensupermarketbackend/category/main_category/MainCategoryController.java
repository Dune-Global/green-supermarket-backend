package com.dune.greensupermarketbackend.category.main_category;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryDto;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryResponseMessageDto;
import com.dune.greensupermarketbackend.category.main_category.service.MainCategoryService;

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
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/main-category")
@AllArgsConstructor
public class MainCategoryController {

    private MainCategoryService mainCategoryService;

    // Get the list of main categories
    @GetMapping("all-categories")
    public ResponseEntity<List<MainCategoryDto>> getAllCategories() {
        List<MainCategoryDto> list = mainCategoryService.getAllCategories();
        return ResponseEntity.ok(list);
    }

    // Get main category by ID
    @GetMapping("{mainCategoryId}")
    public ResponseEntity<MainCategoryDto> getCategoryById(@PathVariable("mainCategoryId") Integer mainCategoryId) {
        MainCategoryDto mainCategoryDto = mainCategoryService.getCategoryById(mainCategoryId);
        return new ResponseEntity<>(mainCategoryDto, HttpStatus.OK);
    }

    // Add a new category
    @PostMapping("add-category")
    public ResponseEntity<MainCategoryResponseMessageDto> addCategory(@RequestBody MainCategoryDto mainCategoryDto) {
        MainCategoryResponseMessageDto mainCategoryResponseMessageDto = mainCategoryService
                .addCategory(mainCategoryDto);
        return new ResponseEntity<>(mainCategoryResponseMessageDto, HttpStatus.CREATED);
    }

    // Update main category
    @PutMapping("update-category/{mainCategoryId}")
    public ResponseEntity<MainCategoryResponseMessageDto> updateCategory(
            @PathVariable("mainCategoryId") Integer mainCategoryId,
            @RequestBody MainCategoryDto updateCategory) {
        MainCategoryResponseMessageDto mainCategoryResponseMessageDto = mainCategoryService
                .updateCategory(mainCategoryId, updateCategory);
        return new ResponseEntity<>(mainCategoryResponseMessageDto, HttpStatus.CREATED);
    }

    // Delete main category
    @DeleteMapping("delete-category/{mainCategoryId}")
    public ResponseEntity<MainCategoryResponseMessageDto> deleteCategory(@PathVariable("mainCategoryId") Integer mainCategoryId) {
        MainCategoryResponseMessageDto mainCategoryResponseMessageDto = mainCategoryService.deleteCategory(mainCategoryId);
        return new ResponseEntity<>(mainCategoryResponseMessageDto , HttpStatus.OK);
    }
}
