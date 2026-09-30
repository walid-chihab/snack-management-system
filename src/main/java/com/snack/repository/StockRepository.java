package com.snack.repository;

import com.snack.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // indique  que cette interface est un composant Spring de type Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
}