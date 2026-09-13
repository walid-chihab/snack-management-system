package com.snack.service;

import com.snack.model.Produit;
import com.snack.model.Stock;
import com.snack.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void verifierStockPourProduit(Produit produit, Integer quantite) {
        // Récupérer les ingrédients du produit
        List<String> ingredients = produit.getIngredientsNecessaires();
        
        for (String ingredient : ingredients) {
            Stock stock = stockRepository.findByNomIngredient(ingredient)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredient));
            
            if (stock.getQuantiteDisponible() < quantite) {
                throw new RuntimeException("Stock insuffisant pour: " + ingredient);
            }
        }
    }

    @Override
    public void reserverStockPourProduit(Produit produit, Integer quantite) {
        List<String> ingredients = produit.getIngredientsNecessaires();
        
        for (String ingredient : ingredients) {
            Stock stock = stockRepository.findByNomIngredient(ingredient)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredient));
            
            stock.setQuantiteDisponible(stock.getQuantiteDisponible() - quantite);
            stock.setDateMiseAJour(LocalDateTime.now());
            stockRepository.save(stock);
        }
    }

    @Override
    public void libererStockPourProduit(Produit produit, Integer quantite) {
        List<String> ingredients = produit.getIngredientsNecessaires();
        
        for (String ingredient : ingredients) {
            Stock stock = stockRepository.findByNomIngredient(ingredient)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredient));
            
            stock.setQuantiteDisponible(stock.getQuantiteDisponible() + quantite);
            stock.setDateMiseAJour(LocalDateTime.now());
            stockRepository.save(stock);
        }
    }

    @Override
    public Stock verifierStock(String ingredientName) {
        return stockRepository.findByNomIngredient(ingredientName)
            .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredientName));
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Stock mettreAJourStock(String ingredientName, Integer quantite) {
        Stock stock = stockRepository.findByNomIngredient(ingredientName)
            .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredientName));
        
        stock.setQuantiteDisponible(quantite);
        stock.setDateMiseAJour(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @Override
    public Stock ajouterIngredient(Stock stock) {
        stock.setDateMiseAJour(LocalDateTime.now());
        return stockRepository.save(stock);
    }
}