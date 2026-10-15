package com.snack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snack.dto.request.StockRequest;
import com.snack.dto.response.StockResponse;
import com.snack.exception.ResourceNotFoundException;
import com.snack.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class) // utiliser uniquement le controller 
class StockControllerTest {

    @Autowired // injection du MockMvc pour simuler les requêtes HTTP
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;

    @MockitoBean 
    private StockService stockService;

    // TEST 1 : GET /api/stocks → 200 + liste
    @Test
    void getAll_shouldReturn200_andListOfStocks() throws Exception {
        StockResponse r1 = StockResponse.builder()
                .id(1L).nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();
        StockResponse r2 = StockResponse.builder()
                .id(2L).nomIngredient("Sucre").quantiteEnStock(30).seuilAlerte(5).build();

        when(stockService.getAll()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nomIngredient").value("Farine"))
                .andExpect(jsonPath("$[1].nomIngredient").value("Sucre"));

        verify(stockService).getAll();
    }

    // TEST 2 : GET /api/stocks/1 → 200
    @Test
    void getById_shouldReturn200_whenStockExists() throws Exception {
        StockResponse response = StockResponse.builder()
                .id(1L).nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();

        when(stockService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/stocks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomIngredient").value("Farine"))
                .andExpect(jsonPath("$.quantiteEnStock").value(50))
                .andExpect(jsonPath("$.seuilAlerte").value(10));

        verify(stockService).getById(1L);
    }

    // TEST 3 : GET /api/stocks/999 → 404
    @Test
    void getById_shouldReturn404_whenStockDoesNotExist() throws Exception {
        when(stockService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Stock not found with id 999"));

        mockMvc.perform(get("/api/stocks/999"))
                .andExpect(status().isNotFound());

        verify(stockService).getById(999L);
    }

    // TEST 4 : POST /api/stocks → 201
    @Test
    void create_shouldReturn201_andCreatedStock() throws Exception {
        StockRequest request = StockRequest.builder()
                .nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();
        StockResponse response = StockResponse.builder()
                .id(1L).nomIngredient("Farine").quantiteEnStock(50).seuilAlerte(10).build();

        when(stockService.create(any(StockRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomIngredient").value("Farine"))
                .andExpect(jsonPath("$.quantiteEnStock").value(50));

        verify(stockService).create(any(StockRequest.class));
    }

    // TEST 5 : PUT /api/stocks/1 → 200
    @Test
    void update_shouldReturn200_whenStockExists() throws Exception {
        StockRequest request = StockRequest.builder()
                .nomIngredient("Farine T55").quantiteEnStock(100).seuilAlerte(20).build();
        StockResponse response = StockResponse.builder()
                .id(1L).nomIngredient("Farine T55").quantiteEnStock(100).seuilAlerte(20).build();

        when(stockService.update(eq(1L), any(StockRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/stocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomIngredient").value("Farine T55"))
                .andExpect(jsonPath("$.quantiteEnStock").value(100));

        verify(stockService).update(eq(1L), any(StockRequest.class));
    }

    // TEST 6 : DELETE /api/stocks/1 → 204
    @Test
    void delete_shouldReturn204_whenStockExists() throws Exception {
        doNothing().when(stockService).delete(1L);

        mockMvc.perform(delete("/api/stocks/1"))
                .andExpect(status().isNoContent());

        verify(stockService).delete(1L);
    }

    // TEST 7 : DELETE /api/stocks/999 → 404
    @Test
    void delete_shouldReturn404_whenStockDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Stock not found with id 999"))
                .when(stockService).delete(999L);

        mockMvc.perform(delete("/api/stocks/999"))
                .andExpect(status().isNotFound());

        verify(stockService).delete(999L);
    }
}