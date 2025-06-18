package me.iliasse.gestion_produits.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import me.iliasse.gestion_produits.dto.product.ProductAdminDto;
import me.iliasse.gestion_produits.dto.product.ProductDetailsDTO;
import me.iliasse.gestion_produits.dto.product.ProductListingDto;
import me.iliasse.gestion_produits.entities.Product;
import me.iliasse.gestion_produits.repository.ProductRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("/admin/products/new")
    public String create(Model model){
        ProductAdminDto productAdminDto = new ProductAdminDto();
        model.addAttribute("productAdminDto", productAdminDto);
        return "views/product/admin/new";
    }

    @PostMapping("/admin/products")
    public String save(@Valid @ModelAttribute("productAdminDto") ProductAdminDto productAdminDto, BindingResult results){
        if(results.hasErrors()){
            return "views/product/admin/new";
        }
        else{
            Product product = Product.builder()
                    .name(productAdminDto.getName())
                    .description(productAdminDto.getDescription())
                    .price(productAdminDto.getPrice())
                    .quantity(productAdminDto.getQuantity())
                    .build();

            this.productRepository.save(product);

            return "redirect:/admin/products";
        }
    }

    @PostMapping("/admin/products/{id}")
    public String delete(@PathVariable Long id){
        try{
            this.productRepository.deleteById(id);
        }
        catch(EmptyResultDataAccessException ex){
            //TODO: message flash
        }
        finally {
            return "redirect:/admin/products";
        }
    }

    @RequestMapping(value="/admin/products/edit/{id}", method={RequestMethod.GET, RequestMethod.POST})
    public String edit(HttpServletRequest request, @PathVariable Long id, Model model, @Valid @ModelAttribute("productAdminDto") ProductAdminDto productAdminDto, BindingResult results){
        try{
            Product product = this.productRepository.findById(id).get();

            if(request.getMethod().equalsIgnoreCase("GET")){
                ProductAdminDto prdtAdminDto = new ProductAdminDto(product);
                model.addAttribute("productAdminDto", prdtAdminDto);

                return "views/product/admin/edit";
            }
            else{
                if(results.hasErrors()){
                    return "views/product/admin/edit";
                }
                else{
                    product.setName(productAdminDto.getName());
                    product.setDescription(productAdminDto.getDescription());
                    product.setPrice(productAdminDto.getPrice());
                    product.setQuantity(productAdminDto.getQuantity());

                    this.productRepository.save(product);

                    return "redirect:/admin/products";
                }
            }
        }
        catch(NoSuchElementException ex){
            return "redirect:/admin/products";
        }
    }
}
