package me.iliasse.gestion_produits.dto.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.iliasse.gestion_produits.entities.Product;
import org.springframework.web.multipart.MultipartFile;

@Setter @Getter @NoArgsConstructor
public final class ProductAdminDto {

    private Long id;

    @NotBlank(message = "Ce champs est requis")
    @Size(min=3, max=70, message="Le nom du produit doit contenir entre 3 et 70 caractères")
    private String name;

    @NotBlank(message = "Ce champs est requis")
    @Size(min = 20, message = "La déscription doit contenir au moin 20 caractères")
    private String description;

    private String image;

    @Positive(message = "Le prix doit être strictement positif")
    private double price;

    @Positive(message = "La quantité doit être strictement positif")
    private int quantity;

    private MultipartFile img;

    public ProductAdminDto(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.image = product.getImage();
    }
}
