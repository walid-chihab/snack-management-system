package com.snack.repository;

import com.snack.model.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    // TEST 1 : save() — créer
    @Test
    void save_shouldPersistStock_andGenerateId() {
        Stock stock = Stock.builder()
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        Stock saved = stockRepository.save(stock);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Farine", saved.getNomIngredient());
        assertEquals(50, saved.getQuantiteEnStock());
        assertEquals(10, saved.getSeuilAlerte());
    }

    // TEST 2 : findById() — existe
    @Test
    void findById_shouldReturnStock_whenExists() {
        Stock stock = stockRepository.save(
                Stock.builder()
                        .nomIngredient("Sucre")
                        .quantiteEnStock(30)
                        .seuilAlerte(5)
                        .build()
        );

        Optional<Stock> result = stockRepository.findById(stock.getId());

        assertTrue(result.isPresent());
        Stock found = result.get();
        assertEquals("Sucre", found.getNomIngredient());
        assertEquals(30, found.getQuantiteEnStock());
    }

    // TEST 3 : findById() — inexistant
    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Stock> result = stockRepository.findById(999L);
        assertTrue(result.isEmpty());
    }

    // TEST 4 : findAll()
    @Test
    void findAll_shouldReturnAllStocks() {
        stockRepository.save(Stock.builder()
                .nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build());
        stockRepository.save(Stock.builder()
                .nomIngredient("Sucre").quantiteEnStock(30).seuilAlerte(5).build());

        List<Stock> result = stockRepository.findAll();

        assertEquals(2, result.size());
    }

    // TEST 5 : existsById() — vrai
    @Test
    void existsById_shouldReturnTrue_whenExists() {
        Stock stock = stockRepository.save(
                Stock.builder()
                        .nomIngredient("Poivre")
                        .quantiteEnStock(10)
                        .seuilAlerte(2)
                        .build()
        );

        assertTrue(stockRepository.existsById(stock.getId()));
    }

    // TEST 6 : existsById() — faux
    @Test
    void existsById_shouldReturnFalse_whenNotExists() {
        assertFalse(stockRepository.existsById(999L));
    }

    // TEST 7 : deleteById()
    @Test
    void deleteById_shouldRemoveStock() {
        Stock stock = stockRepository.save(
                Stock.builder()
                        .nomIngredient("Sel")
                        .quantiteEnStock(20)
                        .seuilAlerte(5)
                        .build()
        );
        Long id = stock.getId();

        stockRepository.deleteById(id);

        assertFalse(stockRepository.existsById(id));
    }

    // TEST 8 : save() — mise à jour
    @Test
    void save_shouldUpdateStock_whenAlreadyExists() {
        Stock stock = stockRepository.save(
                Stock.builder()
                        .nomIngredient("Farine")
                        .quantiteEnStock(50)
                        .seuilAlerte(10)
                        .build()
        );

        stock.setNomIngredient("Farine T55");
        stock.setQuantiteEnStock(100);
        stock.setSeuilAlerte(20);
        Stock updated = stockRepository.save(stock);

        assertEquals(stock.getId(), updated.getId());
        assertEquals("Farine T55", updated.getNomIngredient());
        assertEquals(100, updated.getQuantiteEnStock());
        assertEquals(20, updated.getSeuilAlerte());
    }
}