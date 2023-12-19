package com.dune.greensupermarketbackend.azure_storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Configuration
public class AzureBlobConfig {

    @Value("${spring.azure.storage.connection.string}")
    private String connectionString;
    @Value("${spring.azure.storage.container.name}")
    private String containerName;

    @Bean
    public BlobServiceClient blobServiceClient() {

        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Bean
    public BlobContainerClient blobContainerClient() {

        return blobServiceClient()
                .getBlobContainerClient(containerName);

    }
}