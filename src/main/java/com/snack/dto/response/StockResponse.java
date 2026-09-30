package com.snack.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponse {

    private Long id;

    private String nomIngredient;

    private Integer quantiteEnStock;

    private Integer seuilAlerte;
}