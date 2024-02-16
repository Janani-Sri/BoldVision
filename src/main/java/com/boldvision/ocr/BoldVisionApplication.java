package com.boldvision.ocr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BoldVisionApplication {
	public static void main(String[] args) throws IOException {
		System.setProperty("jna.library.path", "/opt/local/lib");
		System.setProperty("jna.library.path", "/opt/local/lib");
		SpringApplication.run(BoldVisionApplication.class, args);
	}

}
