package me.iliasse.gestion_produits.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString @Builder
public class Product {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @NotNull @Positive
    private double price;

    @NotNull @Positive
    private int quantity;

    @NotNull @Column(columnDefinition = "TEXT")
    private String description;

    //@NotNull @Column(columnDefinition = "TEXT")
    //private String image;
}
