package me.iliasse.gestion_produits.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import me.iliasse.gestion_produits.dto.product.ProductAdminDto;
import me.iliasse.gestion_produits.dto.product.ProductDetailsDTO;
import me.iliasse.gestion_produits.dto.product.ProductListingDto;
import me.iliasse.gestion_produits.entities.Product;
import me.iliasse.gestion_produits.exceptions.TryToDisplayNotPublishedProductException;
import me.iliasse.gestion_produits.repository.ProductRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
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
    public String index(@RequestParam(name="q", required=false) String query, HttpServletRequest request, Model model){
        String route = request.getRequestURI();
        if(route.equals("/products")){
            model.addAttribute("activePage", "products");

            List<ProductListingDto> productListingDto = null;
            if(query != null && !query.isBlank()){
                model.addAttribute("query", query);

                productListingDto = this.productRepository.searchByNameOrPriceButPublishedTrue(query).stream().map(p -> {
                    return new ProductListingDto(p, ProductController.CURRENCY, 120);
                }).toList();;
            }
            else{
                productListingDto = this.productRepository.findByPublishedTrue().stream().map(p -> {
                    return new ProductListingDto(p, ProductController.CURRENCY, 120);
                }).toList();
            }

            model.addAttribute("products", productListingDto);
            return "views/product/index";
        }else{
            if(query != null && !query.isBlank()){
                model.addAttribute("query", query);
                model.addAttribute("products", this.productRepository.searchByNameOrPrice(query));
            }
            else{
                model.addAttribute("products", this.productRepository.findAll());
            }
            model.addAttribute("activePage", "dashboard");
            return "views/product/admin/index";
        }
    }

    @GetMapping("/products/{id}")
    public String show(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        try{
            Product product = this.productRepository.findById(id).get();
            if(!product.isPublished()){
                throw new TryToDisplayNotPublishedProductException();
            }

            ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(product, ProductController.CURRENCY);

            model.addAttribute("activePage", "products");
            model.addAttribute("product", productDetailsDTO);

            return "views/product/show";

        }catch(NoSuchElementException ex){
            redirectAttributes.addFlashAttribute("error", "Il n'existe aucun produit qui son id est " + id);
            return "redirect:/products";
        }
        catch(TryToDisplayNotPublishedProductException ex){
            redirectAttributes.addFlashAttribute("error", "Il n'existe aucun produit qui son id est " + id);
            return "redirect:/products";
        }
    }

    @GetMapping("/admin/products/new")
    public String create(Model model){
        ProductAdminDto productAdminDto = new ProductAdminDto();
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("productAdminDto", productAdminDto);
        return "views/product/admin/new";
    }

    @PostMapping("/admin/products")
    public String save(@Valid @ModelAttribute("productAdminDto") ProductAdminDto productAdminDto, Model model, BindingResult results, RedirectAttributes redirectAttributes){
        model.addAttribute("activePage", "dashboard");

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
                        .published(productAdminDto.isPublished())
                        .build();

                this.productRepository.save(product);
                redirectAttributes.addFlashAttribute("success", "Le produit '" + product.getName() + "' a été ajouté avec succès");
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }

            return "redirect:/admin/products";
        }
    }

    @PostMapping("/admin/products/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            Product product = this.productRepository.findById(id).get();
            this.productRepository.delete(product);

            // Suppression de l'image associée au produit
            redirectAttributes.addFlashAttribute("success", "Le produit '" + product.getName() + "' a été supprimé avec succès");
            this.deleteAssociatedImg(product);

        }
        catch(EmptyResultDataAccessException ex){
            System.out.println(ex.getMessage());
        }
        finally {
            return "redirect:/admin/products";
        }
    }

    @RequestMapping(value="/admin/products/edit/{id}", method={RequestMethod.GET, RequestMethod.POST})
    public String edit(HttpServletRequest request, @PathVariable Long id, Model model, @Valid @ModelAttribute("productAdminDto") ProductAdminDto productAdminDto, BindingResult results, RedirectAttributes redirectAttributes) throws IOException {
        try{
            model.addAttribute("activePage", "dashboard");

            Product product = this.productRepository.findById(id).get();

            if(request.getMethod().equalsIgnoreCase("GET")){
                ProductAdminDto prdtAdminDto = new ProductAdminDto(product);
                model.addAttribute("productAdminDto", prdtAdminDto);

                return "views/product/admin/edit";
            }
            else{
                if(productAdminDto.getImg() == null || productAdminDto.getImg().isEmpty() && product.getImage() != null){
                    productAdminDto.setImage(product.getImage());
                }

                if(results.hasErrors()){
                    return "views/product/admin/edit";
                }
                else{
                    if(productAdminDto.getImg() != null && !productAdminDto.getImg().isEmpty()){
                        this.deleteAssociatedImg(product);
                        String uniqFileName = this.uploadImageAndGetUniqFileName(productAdminDto.getImg());
                        product.setImage(uniqFileName);
                    }

                    product.setName(productAdminDto.getName());
                    product.setDescription(productAdminDto.getDescription());
                    product.setPrice(productAdminDto.getPrice());
                    product.setQuantity(productAdminDto.getQuantity());
                    product.setPublished(productAdminDto.isPublished());

                    this.productRepository.save(product);
                    redirectAttributes.addFlashAttribute("success", "La modification a été effectuée avec succès");

                    return "redirect:/admin/products";
                }
            }
        }
        catch(NoSuchElementException ex){
            redirectAttributes.addFlashAttribute("error", "Il n'existe aucun produit qui sont id est " + id);
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
