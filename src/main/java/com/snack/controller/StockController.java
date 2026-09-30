package com.snack.controller;

import com.snack.dto.request.StockRequest;
import com.snack.dto.response.StockResponse;
import com.snack.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor // Injection propre via le constructeur généré par Lombok
public class StockController {

    private final StockService stockService;

    // GET /api/stocks
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAll() {
        return ResponseEntity.ok(stockService.getAll());
    }

    // GET /api/stocks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getById(id));
    }

    // POST /api/stocks
    @PostMapping
    public ResponseEntity<StockResponse> create(@Valid @RequestBody StockRequest request) {
        StockResponse response = stockService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /api/stocks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<StockResponse> update(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.update(id, request));
    }

    // DELETE /api/stocks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}