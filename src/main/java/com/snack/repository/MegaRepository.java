package com.snack.repository;

import com.snack.model.Mega;
import com.snack.model.TypeMega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MegaRepository extends JpaRepository<Mega, Long> {
    
    // Récupérer les MEGA par type
    List<Mega> findByType(TypeMega type);
    
    // Récupérer les MEGA par prix
    List<Mega> findByPrixLessThanEqual(Double prix);
    
    // Récupérer les MEGA avec frites
    List<Mega> findByAvecFritesTrue();
}