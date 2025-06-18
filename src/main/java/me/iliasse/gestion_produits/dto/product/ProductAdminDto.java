package me.iliasse.gestion_produits.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.iliasse.gestion_produits.entities.Product;

@NoArgsConstructor @Setter @Getter
public final class ProductAdminDto {

    @NotBlank(message = "Ce champs est requis")
    @Size(min=3, max=70, message="Le nom du produit doit contenir entre 3 et 70 caractères")
    private String name;

    @NotBlank(message = "Ce champs est requis")
    @Size(min = 20, message = "La déscription doit contenir au moin 20 caractères")
    private String description;

    //private String image;

    @Positive(message = "Le prix doit être strictement positif")
    private double price;

    @Positive(message = "La quantité doit être strictement positif")
    private int quantity;
}
