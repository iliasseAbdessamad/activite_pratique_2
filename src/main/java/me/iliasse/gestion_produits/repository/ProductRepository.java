package me.iliasse.gestion_produits.repository;

import me.iliasse.gestion_produits.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPublishedTrue();

    @Query("select p from Product p where p.published = true and (Lower(p.name) like lower(concat('%', :query, '%')) or cast(p.price as string) like %:query%)")
    List<Product> searchByNameOrPriceButPublishedTrue(@Param("query") String query);

    @Query("select p from Product p where Lower(p.name) like lower(concat('%', :query, '%')) or cast(p.price as string) like %:query%")
    List<Product> searchByNameOrPrice(@Param("query") String query);
}
