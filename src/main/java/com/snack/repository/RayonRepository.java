package com.snack.repository;

import com.snack.model.Rayon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RayonRepository extends JpaRepository<Rayon, Long> {
    
    // Récupérer les rayons par nom
    List<Rayon> findByNomContaining(String nom);
    
    // Récupérer les rayons d'un étage
    List<Rayon> findByEtageId(Long etageId);
}