package com.dune.greensupermarketbackend.customer;

import com.dune.greensupermarketbackend.ApiVersionConfig;
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


    //Get admin by
    @GetMapping("{id}")
    public ResponseEntity<CustomerDto> getAdmin(@PathVariable("id") Integer id){
        CustomerDto customerDto = customerService.getCustomer(id);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    //Get all admins
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllAdmin(){
        List<CustomerDto> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }

    //Update admin
    @PutMapping("{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Integer id,@RequestBody CustomerDto customerDto){
        CustomerDto updatedCustomer = customerService.updateCustomer(id,customerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    //Delete admin
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Integer id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer Deleted successfully");
    }
}
