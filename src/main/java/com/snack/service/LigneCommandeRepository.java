package com.snack.repository;

import com.snack.model.LigneCommande;
import com.snack.model.Commande;
import com.snack.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    
    // Récupérer toutes les lignes d'une commande
    List<LigneCommande> findByCommande(Commande commande);
    
    // Récupérer toutes les lignes d'une commande par son ID
    List<LigneCommande> findByCommandeId(Long commandeId);
    
    // Récupérer les lignes pour un produit spécifique
    List<LigneCommande> findByProduit(Produit produit);
    
    // Récupérer les lignes pour un produit par son ID
    List<LigneCommande> findByProduitId(Long produitId);
}