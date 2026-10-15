package com.snack.service;

// Imports les classes (les mêmes que dans StockService)
import com.snack.dto.request.StockRequest;
import com.snack.dto.response.StockResponse;
import com.snack.exception.ResourceNotFoundException;
import com.snack.mapper.StockMapper;
import com.snack.model.Stock;
import com.snack.repository.StockRepository;

// Imports JUnit 5
import org.junit.jupiter.api.Test;

// Imports Mockito
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Imports Java standard
import java.util.List;
import java.util.Optional;

// Imports des assertions JUnit (assertEquals, assertNotNull, etc.)
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
// Imports des méthodes Mockito (when, verify, etc.)
import static org.mockito.Mockito.*;

/**
 * ============================================================
 * StockServiceTest
 * ============================================================
 * Cette classe teste UNIQUEMENT la logique de StockService.
 *
 * RAPPEL :
 * - JUnit   → exécute les tests et vérifie les résultats
 * - Mockito → simule les dépendances (Repository, Mapper)
 *
 * On NE teste PAS :
 * - le Controller (ce sera StockControllerTest)
 * - le Repository (ce serait StockRepositoryTest)
 * - la base de données (Mockito l'évite)
 */

@ExtendWith(MockitoExtension.class)  // Active Mockito pour JUnit 5
class StockServiceTest {
 
    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    // TEST 1 : getById() — cas normal
    @Test
    void getById_shouldReturnStockResponse_whenStockExists() {

        Long id = 1L; // oon prepare les donnees du test 

        // comment l'obj sera dans la base de données (le faux Stock)
        Stock stock = Stock.builder()
                .id(id)
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        // ce qu'on attend si tout se passe bien
        StockResponse response = StockResponse.builder()
                .id(id)
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        // On programme le faux Repository :
        // "Quand on appellera findById(1L), renvoie Optional.of(stock)"
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));

        // On programme le mock mapper :
        // quand le service appellera toResponse(stock), on lui renvoie "response"
        // ( le vrai toResponse du StockMapper n'est PAS exécuté ici)
        when(stockMapper.toResponse(stock)).thenReturn(response);

        // ACT — Appeler la méthode à tester
        StockResponse result = stockService.getById(id);

        // ASSERT — Vérifier le résultat (données renvoyées par le service)
        assertNotNull(result);                              // result n'est pas null
        assertEquals(id, result.getId());                   // id = 1L
        assertEquals("Farine", result.getNomIngredient());  // nom = "Farine"
        assertEquals(50, result.getQuantiteEnStock());      // quantite = 50

        // VERIFY — Vérifier que les mocks ont bien été appelés ( les interactions avec les dépendances)
        verify(stockRepository).findById(id);   // findById a été appelé 1 fois
        verify(stockMapper).toResponse(stock);  // toResponse a été appelé 1 fois
    }

    // TEST 2 : getById() — cas "not found"

    @Test
    void getById_shouldThrowResourceNotFoundException_whenStockDoesNotExist() {
        // ARRANGE
        Long id = 999L; 

        // On programme le faux Repository :
        // "Quand on appellera findById(999L), renvoie Optional.empty()"
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        // ACT + ASSERT
        // assertThrows vérifie qu'une exception est lancée
        // utilsant lambda pour appeler id au mommoent de l'appele du assertThrows (execution ) 
        //pas ou moment de evaluation !!! 
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> stockService.getById(id)   // lambda qui appelle la méthode
        );

        // On vérifie le message de l'exception
        //.contains: si 999 exist return true 
        assertTrue(exception.getMessage().contains("999"));

        // VERIFY — le Mapper ne doit JAMAIS être appelé
        verify(stockMapper, never()).toResponse(any(Stock.class));
    }

    // TEST 3 : getAll()

    @Test
    void getAll_shouldReturnListOfStockResponses() {

        // ARRANGE
        Stock stock1 = Stock.builder().id(1L).nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();
        Stock stock2 = Stock.builder().id(2L).nomIngredient("Sucre").quantiteEnStock(30).seuilAlerte(5).build();

        StockResponse response1 = StockResponse.builder().id(1L).nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();
        StockResponse response2 = StockResponse.builder().id(2L).nomIngredient("Sucre").quantiteEnStock(30).seuilAlerte(5).build();
 
        when(stockRepository.findAll()).thenReturn(List.of(stock1, stock2));
        when(stockMapper.toResponse(stock1)).thenReturn(response1);
        when(stockMapper.toResponse(stock2)).thenReturn(response2);

        // ACT
        List<StockResponse> result = stockService.getAll();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Farine", result.get(0).getNomIngredient());
        assertEquals("Sucre", result.get(1).getNomIngredient());

        // VERIFY
        verify(stockRepository).findAll();
        verify(stockMapper, times(2)).toResponse(any(Stock.class));
    }

    // TEST 4 : create()
    @Test
    void create_shouldSaveAndReturnStockResponse() {

        // ARRANGE
        StockRequest request = StockRequest.builder()
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        Stock stockToSave = Stock.builder()
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        Stock savedStock = Stock.builder()
                .id(1L)
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        StockResponse response = StockResponse.builder()
                .id(1L)
                .nomIngredient("Farine")
                .quantiteEnStock(50)
                .seuilAlerte(10)
                .build();

        // Le Mapper transforme Request → Stock
        when(stockMapper.toEntity(request)).thenReturn(stockToSave);
        // Le Repository sauvegarde et renvoie avec un ID
        when(stockRepository.save(stockToSave)).thenReturn(savedStock);
        // Le Mapper transforme Stock → Response
        when(stockMapper.toResponse(savedStock)).thenReturn(response);

        // ACT
        StockResponse result = stockService.create(request);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Farine", result.getNomIngredient());

        // VERIFY
        verify(stockMapper).toEntity(request);
        verify(stockRepository).save(stockToSave);
        verify(stockMapper).toResponse(savedStock);
    }

    // TEST 5 : update() — cas normal
    @Test
    void update_shouldModifyAndReturnStockResponse_whenStockExists() {

        // ARRANGE
        Long id = 1L;
        StockRequest request = StockRequest.builder()
                .nomIngredient("Farine T55")
                .quantiteEnStock(100)
                .seuilAlerte(20)
                .build();

        Stock existingStock = Stock.builder().id(id).nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();
        Stock updatedStock = Stock.builder().id(id).nomIngredient("Farine T55").quantiteEnStock(100).seuilAlerte(20).build();
        StockResponse response = StockResponse.builder().id(id).nomIngredient("Farine T55").quantiteEnStock(100).seuilAlerte(20).build();

        when(stockRepository.findById(id)).thenReturn(Optional.of(existingStock));
        when(stockRepository.save(existingStock)).thenReturn(updatedStock);
        when(stockMapper.toResponse(updatedStock)).thenReturn(response);

        // ACT
        StockResponse result = stockService.update(id, request);

        // ASSERT
        assertNotNull(result);
        assertEquals("Farine T55", result.getNomIngredient());
        assertEquals(100, result.getQuantiteEnStock());

        // VERIFY — le mapper a bien été appelé pour modifier l'entité
        verify(stockMapper).updateEntity(existingStock, request);
        verify(stockRepository).save(existingStock);
    }

    // TEST 6 : delete() — cas normal
    @Test
    void delete_shouldCallRepository_whenStockExists() {

        // ARRANGE
        Long id = 1L;
        when(stockRepository.existsById(id)).thenReturn(true);

        // ACT
        stockService.delete(id);

        // VERIFY
        verify(stockRepository).existsById(id);
        verify(stockRepository).deleteById(id);
    }

    // TEST 7 : delete() — cas "not found"
    @Test
    void delete_shouldThrowResourceNotFoundException_whenStockDoesNotExist() {

        // ARRANGE
        Long id = 999L;
        when(stockRepository.existsById(id)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> stockService.delete(id));

        // VERIFY — on ne doit JAMAIS appeler deleteById
        verify(stockRepository).existsById(id);
        verify(stockRepository, never()).deleteById(anyLong());
    }
}