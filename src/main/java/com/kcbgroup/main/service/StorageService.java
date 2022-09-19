package com.kcbgroup.main.service;

import com.kcbgroup.main.config.FileStorageProperties;
import com.kcbgroup.main.entity.ImageData;
import com.kcbgroup.main.exception.FileStorageException;
import com.kcbgroup.main.exception.MyFileNotFoundException;
import com.kcbgroup.main.repository.StorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * @ AUTHOR MKOECH
 **/
@Service
public class StorageService {
    @Autowired
    private StorageRepo repo;

    public String uploadImage(MultipartFile file){
        try {
           ImageData imageData= repo.save(ImageData.builder()
                    .name(file.getOriginalFilename()).type(file.getContentType()).imageData(file.getBytes()).build());


            if(imageData != null){
                return "image save successfully" +file.getOriginalFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] downloadImage(String filename){

       Optional<ImageData> optional = repo.findByName(filename);
        return optional.map(ImageData::getImageData).orElse(null);

    }


    private final Path fileStorageLocation;

    @Autowired
    public StorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

    }


    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

}
