package com.snack.repository;

import com.snack.model.Etage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtageRepository extends JpaRepository<Etage, Long> {
    
    // Récupérer les étages par numéro
    List<Etage> findByNumero(Integer numero);
}