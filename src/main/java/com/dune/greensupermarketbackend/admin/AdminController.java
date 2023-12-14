package com.dune.greensupermarketbackend.admin;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.admin.dto.AdminDto;
import com.dune.greensupermarketbackend.admin.service.AdminService;
import com.dune.greensupermarketbackend.auth.PasswordUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/admins")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    //Get admin by
    @GetMapping("/search-admins/{empId}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable("empId") String empId){
        AdminDto adminDto = adminService.getAdmin(empId);
        return new ResponseEntity<>(adminDto, HttpStatus.OK);
    }

    //Get all admins
    @GetMapping
    public ResponseEntity<List<AdminDto>> getAllAdmin(){
        List<AdminDto> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    //Update admin
    @PutMapping("{empId}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable("empId") String empId,@RequestBody AdminDto adminDto){
        AdminDto updatedAdmin = adminService.updateAdmin(empId,adminDto);
        return ResponseEntity.ok(updatedAdmin);
    }

    //Delete admin
    @DeleteMapping("/delete-admin/{empId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("empId") String empId){
        adminService.deleteAdmin(empId);
        return ResponseEntity.ok("Admin Deleted successfully");
    }

    //Update admin password
    @PatchMapping("update-password/{empId}")
    public ResponseEntity<String> updatePassword(@PathVariable("empId") String empId, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        adminService.updatePassword(empId, passwordUpdateRequest);
        return ResponseEntity.ok("Password Update successful");
    }
}