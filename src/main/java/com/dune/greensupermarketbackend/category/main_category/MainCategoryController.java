package com.dune.greensupermarketbackend.category.main_category;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryDto;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryResponseMessageDto;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryWithSubsDto;
import com.dune.greensupermarketbackend.category.main_category.service.MainCategoryService;

import java.util.ArrayList;
import java.util.List;

import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneWithSubsDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.service.CategoryOneService;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.service.CategoryTwoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/main-category")
@AllArgsConstructor
public class MainCategoryController {

    private MainCategoryService mainCategoryService;
    private final CategoryOneService categoryOneService;
    private final CategoryTwoService categoryTwoService;
    private ModelMapper modelMapper;


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
    public ResponseEntity<MainCategoryResponseMessageDto> addCategory(@RequestBody MainCategoryDto mainCategoryDto,@RequestHeader("imgUrl") String imgUrl) {
        MainCategoryResponseMessageDto mainCategoryResponseMessageDto = mainCategoryService
                .addCategory(mainCategoryDto,imgUrl);
        return new ResponseEntity<>(mainCategoryResponseMessageDto, HttpStatus.CREATED);
    }

    // Update main category
    @PutMapping("update-category/{mainCategoryId}")
    public ResponseEntity<MainCategoryResponseMessageDto> updateCategory(
            @PathVariable("mainCategoryId") Integer mainCategoryId,
            @RequestBody MainCategoryDto updateCategory,
            @RequestHeader("imgUrl") String imgUrl) {
        MainCategoryResponseMessageDto mainCategoryResponseMessageDto = mainCategoryService
                .updateCategory(mainCategoryId, updateCategory,imgUrl);
        return new ResponseEntity<>(mainCategoryResponseMessageDto, HttpStatus.CREATED);
    }

    // Delete main category
    @DeleteMapping("delete-category/{mainCategoryId}")
    public ResponseEntity<MainCategoryResponseMessageDto> deleteCategory(@PathVariable("mainCategoryId") Integer mainCategoryId) {
        MainCategoryResponseMessageDto mainCategoryResponseMessageDto = mainCategoryService.deleteCategory(mainCategoryId);
        return new ResponseEntity<>(mainCategoryResponseMessageDto , HttpStatus.OK);
    }

    @GetMapping("/main-categories-with-sub")
    public List<MainCategoryWithSubsDto> getAllMainCategories() {
    List<MainCategoryDto> mainCategories = mainCategoryService.getAllCategories();
    List<MainCategoryWithSubsDto> mainCategoriesWithSubs = new ArrayList<>();
    for (MainCategoryDto mainCategory : mainCategories) {
        List<CategoryOneDto> categoryOnes = categoryOneService.getAllByMainCategory(mainCategory.getMainCategoryId());
        List<CategoryOneWithSubsDto> categoryOneWithSubs = categoryOnes.stream().map(categoryOneDto -> modelMapper.map(categoryOneDto,CategoryOneWithSubsDto.class)).toList();
        for (CategoryOneWithSubsDto categoryOne : categoryOneWithSubs) {
            List<CategoryTwoDto> categoryTwos = categoryTwoService.getAllBySubCatOne(categoryOne.getSubCatOneId());
            categoryOne.setCategoryTwos(categoryTwos);
        }
        MainCategoryWithSubsDto mainCategoryWithSubs = new MainCategoryWithSubsDto(
                mainCategory.getMainCategoryId(),
                mainCategory.getMainCategoryName(),
                mainCategory.getMainCategoryDesc(),
                mainCategory.getImgUrl(),
                categoryOneWithSubs);
        mainCategoriesWithSubs.add(mainCategoryWithSubs);
    }
    return mainCategoriesWithSubs;
    }

    @GetMapping("/subs/{mainCategoryId}")
    public List<MainCategoryWithSubsDto> getAllMainCategories(@PathVariable("mainCategoryId") Integer mainCategoryId) {
        MainCategoryDto mainCategory = mainCategoryService.getCategoryById(mainCategoryId);
        List<MainCategoryWithSubsDto> mainCategoriesWithSubs = new ArrayList<>(); // Initialize the list

        List<CategoryOneDto> categoryOnes = categoryOneService.getAllByMainCategory(mainCategory.getMainCategoryId());
        List<CategoryOneWithSubsDto> categoryOneWithSubs = categoryOnes.stream().map(categoryOneDto -> modelMapper.map(categoryOneDto,CategoryOneWithSubsDto.class)).toList();
        for (CategoryOneWithSubsDto categoryOne : categoryOneWithSubs) {
            List<CategoryTwoDto> categoryTwos = categoryTwoService.getAllBySubCatOne(categoryOne.getSubCatOneId());
            categoryOne.setCategoryTwos(categoryTwos);
        }
        MainCategoryWithSubsDto mainCategoryWithSubs = new MainCategoryWithSubsDto(
                mainCategory.getMainCategoryId(),
                mainCategory.getMainCategoryName(),
                mainCategory.getMainCategoryDesc(),
                mainCategory.getImgUrl(),
                categoryOneWithSubs);
        mainCategoriesWithSubs.add(mainCategoryWithSubs);

        return mainCategoriesWithSubs;
    }


}
