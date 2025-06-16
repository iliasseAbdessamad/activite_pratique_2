package me.iliasse.gestion_produits.repository;

import me.iliasse.gestion_produits.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
