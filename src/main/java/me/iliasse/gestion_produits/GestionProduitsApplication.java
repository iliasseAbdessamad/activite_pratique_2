package me.iliasse.gestion_produits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication
public class GestionProduitsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestionProduitsApplication.class, args);
	}
}