package com.kcbgroup.main.controller;

import com.kcbgroup.main.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @ AUTHOR MKOECH
 **/
@RestController
@RequestMapping("/v1/api")
@Slf4j
public class LoadImageController {
    @Autowired
    private StorageService service;

    @GetMapping("/download/image/{filename}")
    public ResponseEntity<?> downloadImage (@PathVariable("filename") String filename){
        byte[] image = service.downloadImage(filename);
        return ResponseEntity.status(HttpStatus.OK).contentType(
                MediaType.valueOf("image/png")
        ).body(image);


    }
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file ){

        System.out.println(file.getContentType());

        System.out.println(file.getSize());
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        try {
            System.out.println(file.getInputStream());
        }catch (IOException e){
            e.getMessage();
        }


        //String pathDirectory = "C:\\Users\\KEN20957\\Documents\\PersonalProjects\\load-files-to-server\\load-files-to-server\\src\\main\\resources\\static";


        //storing image in a file
//        try {
//         String   pathDirectory = new ClassPathResource("static").getFile().getAbsolutePath();
//            Files.copy(file.getInputStream(), Paths.get(pathDirectory+ File.separator+file.getOriginalFilename()),
//                    StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            e.getMessage();
//        }
        log.info("***************Store image in database");
        String uploadImage = service.uploadImage(file);


      //  return ResponseEntity.ok(uploadImage);
        return new ResponseEntity<>(uploadImage,HttpStatus.CREATED);
    }




}
