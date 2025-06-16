package me.iliasse.gestion_produits;

import me.iliasse.gestion_produits.entities.Product;
import me.iliasse.gestion_produits.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GestionProduitsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionProduitsApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ProductRepository productRepository){
		return (String... args) -> {
			try{
				productRepository.save(Product.builder().name("Playstation 5").price(2100).quantity(23).build());
				productRepository.save(Product.builder().name("Xbox One").price(1700).quantity(30).build());
				productRepository.save(Product.builder().name("Azus Gamer").price(15000).quantity(10).build());
			}
			catch(Exception ex){
				System.out.println(ex.getClass().getCanonicalName());
				System.out.println(ex.getMessage());
			}
		};
	}
}
