package com.snack.mapper;

import com.snack.dto.request.StockRequest;
import com.snack.dto.response.StockResponse;
import com.snack.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    // StockRequest → Stock (pour POST)
    public Stock toEntity(StockRequest request) {
        if (request == null) return null;
        return Stock.builder()
                .nomIngredient(request.getNomIngredient())
                .quantiteEnStock(request.getQuantiteEnStock())
                .seuilAlerte(request.getSeuilAlerte())
                .build();
    }

    // Stock → StockResponse (pour GET)
    public StockResponse toResponse(Stock stock) {
        if (stock == null) return null;
        return StockResponse.builder()
                .id(stock.getId())
                .nomIngredient(stock.getNomIngredient())
                .quantiteEnStock(stock.getQuantiteEnStock())
                .seuilAlerte(stock.getSeuilAlerte())
                .build();
    }

    // Stock + StockRequest → Stock modifié (pour PUT)
    public void updateEntity(Stock stock, StockRequest request) {
        if (stock == null || request == null) return;
        stock.setNomIngredient(request.getNomIngredient());
        stock.setQuantiteEnStock(request.getQuantiteEnStock());
        stock.setSeuilAlerte(request.getSeuilAlerte());
    }
}