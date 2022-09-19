package com.kcbgroup.main.controller;

import com.kcbgroup.main.entity.UploadFileResponse;
import com.kcbgroup.main.service.impl.StorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ AUTHOR MKOECH
 **/
@RestController
@RequestMapping("/v1/api")
@Slf4j
public class LoadImageController {

    @Autowired
    private StorageServiceImpl service;



    //****************Uploading and  downloading  files to a database************//


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
            System.out.println(Arrays.toString(file.getBytes()));
            System.out.println(file.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }


       log.info("***************Store image in database");
       String uploadImage = service.uploadImage(file);

        return new ResponseEntity<>(uploadImage,HttpStatus.CREATED);

    }


    //************************Uploading and Downloading file to a local file directory*******//

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile (@RequestParam("file") MultipartFile file){
        String filename = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/api/downloadFile/")
                .path(filename)
                .toUriString();

        return  UploadFileResponse.builder()
                .fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType())
                .fileName(filename)
                .size(file.getSize())
                .build();

//        return new UploadFileResponse(filename,fileDownloadUri,file.getContentType(),
//        file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    private List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource


        log.info("FILE NAME: {}", fileName);
        Resource resource = service.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
