package com.kcbgroup.main.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ AUTHOR MKOECH
 **/
public interface StorageService {


    String uploadImage(MultipartFile file);
    byte[] downloadImage(String filename);
    String storeFile(MultipartFile file);
    Resource loadFileAsResource(String fileName);

}
