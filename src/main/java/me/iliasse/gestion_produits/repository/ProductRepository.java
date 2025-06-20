package me.iliasse.gestion_produits.repository;

import me.iliasse.gestion_produits.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPublishedTrue();
}
