package com.kcbgroup.main.service;

import com.kcbgroup.main.entity.ImageData;
import com.kcbgroup.main.repository.StorageRepo;
import com.kcbgroup.main.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

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
                    .name(file.getOriginalFilename()).type(file.getContentType()).imageData(ImageUtils.compressImage(file.getBytes())).build());
            if(imageData != null){
                return "image save successfully" +file.getOriginalFilename();
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return null;
    }

    public byte[] downloadImage(String filename){

       Optional<ImageData> optional = repo.findByName(filename);
       if (optional.isPresent()){
           byte[] image = ImageUtils.decompressImage(optional.get().getImageData());
           return image;
       }
       return null;

    }



}
