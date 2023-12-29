package com.dune.greensupermarketbackend.azure_storage.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.blob.models.BlobHttpHeaders;

@Component
public class AzureBlobService {

    @Autowired
    BlobServiceClient blobServiceClient;

    @Autowired
    BlobContainerClient blobContainerClient;

    public String upload(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String newFileName = UUID.randomUUID().toString() + extension;

        BlobClient blob = blobContainerClient.getBlobClient(newFileName);

        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentType(multipartFile.getContentType());

        blob.uploadWithResponse(new BlobParallelUploadOptions(multipartFile.getInputStream())
                .setHeaders(headers), null);

        String baseUrl = "https://greensupermarketstoreacc.blob.core.windows.net/greensupermarketblogcontainer/";
        return baseUrl + newFileName;
    }

    public byte[] getFile(String fileName)
            throws URISyntaxException {

        BlobClient blob = blobContainerClient.getBlobClient(fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.download(outputStream);
        final byte[] bytes = outputStream.toByteArray();
        return bytes;

    }

    public List<String> listBlobs() {
        PagedIterable<BlobItem> items = blobContainerClient.listBlobs();
        List<String> urls = new ArrayList<>();
        String baseUrl = "https://greensupermarketstoreacc.blob.core.windows.net/greensupermarketblogcontainer/";
        for (BlobItem item : items) {
            urls.add(baseUrl + item.getName());
        }
        return urls;
    }

    public Boolean deleteBlob(String blobName) {
        BlobClient blob = blobContainerClient.getBlobClient(blobName);
        blob.delete();
        return true;
    }

}