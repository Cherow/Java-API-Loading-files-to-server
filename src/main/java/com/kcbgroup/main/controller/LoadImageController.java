package com.kcbgroup.main.controller;

import org.springframework.core.io.ClassPathResource;
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
public class LoadImageController {
    @PostMapping("/upload-image")
    public  String uploadImage(@RequestParam("file")MultipartFile file ){

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

        try {
         String   pathDirectory = new ClassPathResource("static").getFile().getAbsolutePath();
            Files.copy(file.getInputStream(), Paths.get(pathDirectory+ File.separator+file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.getMessage();
        }



        return "successfully image is uploaded";
    }
}
