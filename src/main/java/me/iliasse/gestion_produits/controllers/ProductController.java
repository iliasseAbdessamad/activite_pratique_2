package me.iliasse.gestion_produits.controllers;

import me.iliasse.gestion_produits.dto.product.ProductListingDto;
import me.iliasse.gestion_produits.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String index(Model model){
        Set<ProductListingDto> productListingDto = productRepository.findAll().stream().map(p -> {
            return new ProductListingDto(p, "MAD", 120);
        }).collect(Collectors.toSet());

        model.addAttribute("products", productListingDto);

        return "views/product/index";
    }
}
