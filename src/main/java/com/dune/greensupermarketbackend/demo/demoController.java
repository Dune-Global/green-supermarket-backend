package com.dune.greensupermarketbackend.demo;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/demo")
@AllArgsConstructor
public class demoController {
    @GetMapping()
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("hjljkhjhjk");
    }
}
