package com.snack.service;

import com.snack.dto.request.StockRequest;
import com.snack.dto.response.StockResponse;
import com.snack.exception.ResourceNotFoundException;
import com.snack.mapper.StockMapper;
import com.snack.model.Stock;
import com.snack.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    // GET tous les stocks
    public List<StockResponse> getAll() {
        return stockRepository.findAll()
                .stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    // GET un stock par ID
    public StockResponse getById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec l'id : " + id));
        return stockMapper.toResponse(stock);
    }

    // POST créer un stock
    public StockResponse create(StockRequest request) {
        Stock stock = stockMapper.toEntity(request);
        Stock saved = stockRepository.save(stock);
        return stockMapper.toResponse(saved);
    }

    // PUT modifier un stock
    public StockResponse update(Long id, StockRequest request) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec l'id : " + id));
        stockMapper.updateEntity(stock, request);
        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }

    // DELETE supprimer un stock
    public void delete(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stock non trouvé avec l'id : " + id);
        }
        stockRepository.deleteById(id);
    }
}