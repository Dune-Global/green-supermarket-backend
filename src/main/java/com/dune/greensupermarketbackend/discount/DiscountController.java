package com.dune.greensupermarketbackend.discount;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
import com.dune.greensupermarketbackend.discount.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/discount")
@AllArgsConstructor
public class DiscountController {

    private DiscountService discountService;
    @PostMapping("add-discount")
    public ResponseEntity<DiscountDto> addDiscount(@RequestBody DiscountDto discountDto){
        DiscountDto discount = discountService.createDiscount(discountDto);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("get-discounts")
    public ResponseEntity<List<DiscountDto>> getAllDiscounts(){
        List<DiscountDto> discount = discountService.getAllDiscounts();
        return ResponseEntity.ok(discount);
    }

    @GetMapping("get-discount/{discountId}")
    public ResponseEntity<DiscountDto> getDiscountByDiscountId(@PathVariable("discountId") Integer discountId){
        DiscountDto discount = discountService.getDiscountByDiscountId(discountId);
        return ResponseEntity.ok(discount);
    }

    @PatchMapping("update-discount/{discountId}")
    public ResponseEntity<DiscountDto> updateDiscount(@PathVariable("discountId") Integer discountId,@RequestBody DiscountDto updatedDiscount){
        DiscountDto discount = discountService.updateDiscount(discountId,updatedDiscount);
        return ResponseEntity.ok(discount);
    }

    @DeleteMapping("delete-discount/{discountId}")
    public ResponseEntity<String> deleteDiscount(@PathVariable("discountId") Integer discountId){
        discountService.deleteDiscount(discountId);
        return ResponseEntity.ok("Discount Deleted Successfully");
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<List<DiscountDto>> getDiscountsByProductId(@PathVariable("productId") Integer productId){
        List<DiscountDto> discount = discountService.getDiscountsByProductId(productId);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("current-product/{productId}")
    public ResponseEntity<DiscountDto> getCurrentDiscountForProduct(@PathVariable("productId") Integer productId){
        DiscountDto discount = discountService.getCurrentDiscountForProduct(productId);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("current-discounts")
    public ResponseEntity<List<DiscountDto>> getCurrentDiscounts(){
        List<DiscountDto> discount = discountService.getCurrentDiscounts();
        return ResponseEntity.ok(discount);
    }
}
