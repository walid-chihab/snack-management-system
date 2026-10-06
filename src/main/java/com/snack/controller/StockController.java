package com.snack.controller;

import com.snack.dto.request.StockRequest;
import com.snack.dto.response.StockResponse;
import com.snack.service.StockService;

// SWAGGER : imports des annotations OpenAPI (ne viennent PAS de Spring)
import io.swagger.v3.oas.annotations.Operation;              // SWAGGER
import io.swagger.v3.oas.annotations.Parameter;              // SWAGGER
import io.swagger.v3.oas.annotations.responses.ApiResponse;   // SWAGGER
import io.swagger.v3.oas.annotations.responses.ApiResponses;  // SWAGGER
import io.swagger.v3.oas.annotations.tags.Tag;               // SWAGGER

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Tag(name = "Stock Management", description = "Endpoints for managing stock items") // SWAGGER
public class StockController {

    private final StockService stockService;

    // GET /api/stocks
    @GetMapping
    @Operation(summary = "Get all stocks", description = "Returns a list of all stock items") // SWAGGER
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")           // SWAGGER
    public ResponseEntity<List<StockResponse>> getAll() {
        return ResponseEntity.ok(stockService.getAll());
    }

    // GET /api/stocks/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Get stock by ID", description = "Returns a single stock item") // SWAGGER
    @ApiResponses(value = {                                                              // SWAGGER
        @ApiResponse(responseCode = "200", description = "Stock found"),                 // SWAGGER
        @ApiResponse(responseCode = "404", description = "Stock not found")              // SWAGGER
    })                                                                                   // SWAGGER
    public ResponseEntity<StockResponse> getById(
            @Parameter(description = "ID of the stock to retrieve", example = "1") // SWAGGER
            @PathVariable Long id) { 
        return ResponseEntity.ok(stockService.getById(id));
    }

    // POST /api/stocks
    @PostMapping 
    @Operation(summary = "Create a new stock", description = "Creates a new stock item and returns it") // SWAGGER
    @ApiResponse(responseCode = "201", description = "Stock created successfully")                      // SWAGGER
    public ResponseEntity<StockResponse> create(@Valid @RequestBody StockRequest request) {
        StockResponse response = stockService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /api/stocks/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing stock", description = "Updates a stock item by ID") // SWAGGER
    @ApiResponses(value = {                                                                     // SWAGGER
        @ApiResponse(responseCode = "200", description = "Stock updated"),                      // SWAGGER
        @ApiResponse(responseCode = "404", description = "Stock not found")                     // SWAGGER
    })                                                                                          // SWAGGER
    public ResponseEntity<StockResponse> update(
            @Parameter(description = "ID of the stock to update", example = "1") // SWAGGER
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.update(id, request));
    }

    // DELETE /api/stocks/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a stock", description = "Deletes a stock item by ID") // SWAGGER
    @ApiResponses(value = {                                                            // SWAGGER
        @ApiResponse(responseCode = "204", description = "Stock deleted"),             // SWAGGER
        @ApiResponse(responseCode = "404", description = "Stock not found")            // SWAGGER
    })                                                                                 // SWAGGER
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the stock to delete", example = "1") // SWAGGER
            @PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}