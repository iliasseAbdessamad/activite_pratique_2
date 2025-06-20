package me.iliasse.gestion_produits.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class CreateUploadDirectoryConfig {
    private static final String UPLOAD_DIR = "uploads";

    @Bean
    public CommandLineRunner createUploadDirectory() {
        return args -> {
            Path uploadDirPath = Paths.get(UPLOAD_DIR);
            if(!Files.exists(uploadDirPath)){
                Files.createDirectories(uploadDirPath);
                System.out.println("The " + uploadDirPath.toAbsolutePath() + " is created");
            }
            else{
                System.out.println("The " + uploadDirPath.toAbsolutePath() + " already exists");
            }
        };
    }
}
