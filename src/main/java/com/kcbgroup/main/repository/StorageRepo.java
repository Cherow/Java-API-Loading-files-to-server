package com.kcbgroup.main.repository;

import com.kcbgroup.main.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @ AUTHOR MKOECH
 **/
public interface StorageRepo extends JpaRepository<ImageData,Integer> {
   public  Optional<ImageData> findByName(String fileName);
}
