package com.snack.repository;

import com.snack.model.Tacos;
import com.snack.model.TypeTacos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacosRepository extends JpaRepository<Tacos, Long> {
    
    // Récupérer les TACOS par type
    List<Tacos> findByType(TypeTacos type);
    
    // Récupérer les TACOS par prix
    List<Tacos> findByPrixLessThanEqual(Double prix);
    
    // Récupérer les TACOS gratinés
    List<Tacos> findByGratinerTrue();
    
    // Récupérer les TACOS avec polet
    List<Tacos> findByAvecPoletTrue();
}