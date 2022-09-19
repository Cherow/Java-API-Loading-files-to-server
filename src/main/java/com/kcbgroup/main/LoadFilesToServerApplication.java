package com.kcbgroup.main;

import com.kcbgroup.main.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class LoadFilesToServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadFilesToServerApplication.class, args);
	}

}
