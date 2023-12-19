package com.dune.greensupermarketbackend.azure_storage;

import java.util.List;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.azure_storage.service.AzureBlobService;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/file-storage")
public class AzureController {

    @Autowired
    private AzureBlobService azureBlobAdapter;

    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file)
            throws IOException {

        String fileName = azureBlobAdapter.upload(file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("all-files")
    public ResponseEntity<List<String>> getAllBlobs() {

        List<String> items = azureBlobAdapter.listBlobs();
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Boolean> delete(@RequestParam String fileName) {

        azureBlobAdapter.deleteBlob(fileName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile(@RequestParam String fileName)
            throws URISyntaxException {

        ByteArrayResource resource = new ByteArrayResource(azureBlobAdapter
                .getFile(fileName));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""
                        + fileName + "\"");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers).body(resource);
    }
}