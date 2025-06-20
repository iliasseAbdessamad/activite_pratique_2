package me.iliasse.gestion_produits.dto.product;

import lombok.Getter;
import me.iliasse.gestion_produits.entities.Product;

@Getter
public final class ProductDetailsDTO extends ProductDto {

    private String formattedDescription;
    private int quantity;
    private String image;

    public ProductDetailsDTO(Product product, String currency) {
        super(product, currency);
        this.formattedDescription = this.formatDescription(product.getDescription());
        this.quantity = product.getQuantity();
        this.image = product.getImage();
    }

    private String formatDescription(String description){
        return description.replace("\n", "<br />");
    }
}
