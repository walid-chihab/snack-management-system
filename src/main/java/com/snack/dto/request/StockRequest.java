package com.snack.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockRequest {

    @NotBlank(message = "Le nom de l'ingrédient est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nomIngredient;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité doit être positive ou nulle")
    private Integer quantiteEnStock;

    @NotNull(message = "Le seuil d'alerte est obligatoire")
    @Min(value = 0, message = "Le seuil doit être positif ou nul")
    private Integer seuilAlerte;
}