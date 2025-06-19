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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private static final String CURRENCY = "MAD";

    private ProductRepository productRepository;
    private final static Path uploadPath = Paths.get("uploads");


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
        if(productAdminDto.getImg() == null || productAdminDto.getImg().isEmpty()){
            results.rejectValue("img", "img.empty", "Une image du produit est requise");
        }

        if(results.hasErrors()){
            return "views/product/admin/new";
        }
        else{
            try{
                //uploads image
                String uniqFileName = this.uploadImageAndGetUniqFileName(productAdminDto.getImg());

                //save product
                Product product = Product.builder()
                        .name(productAdminDto.getName())
                        .description(productAdminDto.getDescription())
                        .price(productAdminDto.getPrice())
                        .quantity(productAdminDto.getQuantity())
                        .image(uniqFileName)
                        .build();

                this.productRepository.save(product);
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }


            return "redirect:/admin/products";
        }
    }

    @PostMapping("/admin/products/{id}")
    public String delete(@PathVariable Long id){
        try{
            Product product = this.productRepository.findById(id).get();
            this.productRepository.delete(product);

            // Suppression de l'image associée au produit
            this.deleteAssociatedImg(product);

        }
        catch(EmptyResultDataAccessException ex){
            //TODO: message flash
        }
        finally {
            return "redirect:/admin/products";
        }
    }

    @RequestMapping(value="/admin/products/edit/{id}", method={RequestMethod.GET, RequestMethod.POST})
    public String edit(HttpServletRequest request, @PathVariable Long id, Model model, @Valid @ModelAttribute("productAdminDto") ProductAdminDto productAdminDto, BindingResult results) throws IOException {
        try{
            Product product = this.productRepository.findById(id).get();

            if(request.getMethod().equalsIgnoreCase("GET")){
                ProductAdminDto prdtAdminDto = new ProductAdminDto(product);
                model.addAttribute("productAdminDto", prdtAdminDto);

                return "views/product/admin/edit";
            }
            else{
                if(productAdminDto.getImg() == null || productAdminDto.getImg().isEmpty()){
                    results.rejectValue("img", "img.empty", "Une image du produit est requise");
                }

                if(results.hasErrors()){
                    return "views/product/admin/edit";
                }
                else{
                    //delete old one and then...
                    this.deleteAssociatedImg(product);

                    //... upload the new one
                    String uniqFileName = this.uploadImageAndGetUniqFileName(productAdminDto.getImg());

                    product.setName(productAdminDto.getName());
                    product.setDescription(productAdminDto.getDescription());
                    product.setPrice(productAdminDto.getPrice());
                    product.setQuantity(productAdminDto.getQuantity());
                    product.setImage(uniqFileName);

                    this.productRepository.save(product);

                    return "redirect:/admin/products";
                }
            }
        }
        catch(NoSuchElementException ex){
            return "redirect:/admin/products";
        }
    }

    private String uploadImageAndGetUniqFileName(MultipartFile img) {
        InputStream stream = null;
        try{
            String originalFilename = img.getOriginalFilename();
            String ext =  originalFilename.substring(originalFilename.lastIndexOf('.'));
            String uniqFileName = "img_"+ UUID.randomUUID() + ext;

            stream = img.getInputStream();

            Path filePath = uploadPath.resolve(uniqFileName);
            Files.copy(stream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return uniqFileName;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally{
            try{
                stream.close();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    private void deleteAssociatedImg(Product product){
        String imageName = product.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            Path imagePath = Paths.get(uploadPath.toString(), imageName);
            File imageFile = imagePath.toFile();
            if (imageFile.exists()) {
                try {
                    Files.delete(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
