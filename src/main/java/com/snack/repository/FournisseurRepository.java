package com.snack.repository;

import com.snack.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    
    // Récupérer les fournisseurs par nom
    List<Fournisseur> findByNomContaining(String nom);
    
    // Récupérer les fournisseurs par ville
    List<Fournisseur> findByVille(String ville);
}