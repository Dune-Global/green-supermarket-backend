package com.dune.greensupermarketbackend.customer;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.auth.PasswordUpdateRequest;
import com.dune.greensupermarketbackend.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/customers")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    //Get customer by
    @GetMapping("{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") Integer id){
        CustomerDto customerDto = customerService.getCustomer(id);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    //Get all customer
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers(){
        List<CustomerDto> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }

    //Update customer
    @PutMapping("{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Integer id,@RequestBody CustomerDto customerDto){
        CustomerDto updatedCustomer = customerService.updateCustomer(id,customerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    //Delete customer
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Integer id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer Deleted successfully");
    }

    //Update Customer password
    @PatchMapping("update-password/{customer-id}")
    public ResponseEntity<String> updatePassword(@PathVariable("customer-id") Integer customerId, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        customerService.updatePassword(customerId, passwordUpdateRequest);
        return ResponseEntity.ok("Password Update successful");
    }
}
