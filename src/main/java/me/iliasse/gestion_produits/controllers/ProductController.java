package me.iliasse.gestion_produits.controllers;

import jakarta.servlet.http.HttpServletRequest;
import me.iliasse.gestion_produits.dto.product.ProductDetailsDTO;
import me.iliasse.gestion_produits.dto.product.ProductListingDto;
import me.iliasse.gestion_produits.entities.Product;
import me.iliasse.gestion_produits.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ProductController {

    private static final String CURRENCY = "MAD";

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping({"/products", "/admin/products"})
    public String index(HttpServletRequest request, Model model){
        String route = request.getRequestURI();
        if(route.equals("/products")){
            Set<ProductListingDto> productListingDto = productRepository.findAll().stream().map(p -> {
                return new ProductListingDto(p, ProductController.CURRENCY, 120);
            }).collect(Collectors.toSet());

            model.addAttribute("products", productListingDto);

            return "views/product/index";
        }else{
            model.addAttribute("products", this.productRepository.findAll());
            return "views/product/admin/index";
        }
    }

    @GetMapping("/products/{id}")
    public String show(@PathVariable Long id, Model model){
        try{
            Product product = this.productRepository.findById(id).get();
            ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(product, ProductController.CURRENCY);

            model.addAttribute("product", productDetailsDTO);

            return "views/product/show";

        }catch(NoSuchElementException ex){
            return "redirect:/products";
        }
    }
}
