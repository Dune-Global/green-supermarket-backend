package com.dune.greensupermarketbackend.product_rating;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.product_rating.dto.RatingDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingForProductDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingWithCustomerDto;
import com.dune.greensupermarketbackend.product_rating.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/rating")
@AllArgsConstructor
public class RatingController {
    private RatingService ratingService;

    @PostMapping("new")
    public ResponseEntity<RatingDto> addRating(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(ratingService.addRating(ratingDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<RatingDto> updateRating(@PathVariable Integer id, @RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(ratingService.updateRating(id, ratingDto));
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<RatingForProductDto> getAverageRatingByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(ratingService.getAverageRatingByProductId(productId));
    }

    @GetMapping("product-all/{productId}")
    public ResponseEntity<List<RatingWithCustomerDto>> getRatingProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(ratingService.getRatingProductId(productId));
    }

}
