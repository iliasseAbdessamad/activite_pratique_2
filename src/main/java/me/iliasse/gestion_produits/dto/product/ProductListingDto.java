package me.iliasse.gestion_produits.dto.product;


import lombok.Getter;
import me.iliasse.gestion_produits.entities.Product;


@Getter
public final class ProductListingDto extends ProductDto{

    private String shortDescription;
    private String image;

    public ProductListingDto(Product product, String currency, int limitCharsOfShortDescription) {
        super(product, currency);
        this.shortDescription = this.setShortDescription(product.getDescription(), limitCharsOfShortDescription);
        this.image = product.getImage();
    }

    protected String setShortDescription(String description, int limit){
        if(description != null){
            if(description.length() - 1 <= limit){
                return description;
            }
            else{
                return description.substring(0, limit) + " ...";
            }
        }
        return null;
    }
}
