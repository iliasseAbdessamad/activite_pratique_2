package me.iliasse.gestion_produits.dto.product;


import lombok.Getter;
import me.iliasse.gestion_produits.entities.Product;

import java.text.DecimalFormat;


@Getter
public abstract class ProductDto {

    protected Long id;
    protected String name;
    protected String formattedPrice;

    public ProductDto(Product product, String currency){
        this.id = product.getId();
        this.name = product.getName();
        this.formattedPrice =  this.formatPrice(product.getPrice(), currency);
    }

    protected String formatPrice(double price, String currency){
        return new DecimalFormat("0.00").format(price) + " " + currency;
    }
}
