package com.snack.repository;

import com.snack.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    // Rechercher un ingrédient par son nom (UNIQUE)
    Optional<Stock> findByNomIngredient(String nomIngredient);
    
    // Récupérer les ingrédients avec stock faible (en dessous du seuil)
    List<Stock> findByQuantiteDisponibleLessThanEqual(Integer seuil);
    
    // Récupérer les ingrédients en rupture de stock
    List<Stock> findByQuantiteDisponible(Integer quantite);
    
    // Récupérer les ingrédients avec stock > seuil
    List<Stock> findByQuantiteDisponibleGreaterThan(Integer quantite);
    
    // Recherche par nom contenant un mot-clé
    List<Stock> findByNomIngredientContaining(String keyword);
    
    // Vérifier si un ingrédient existe
    boolean existsByNomIngredient(String nomIngredient);
    
    // Mettre à jour le stock d'un ingrédient (requête personnalisée)
    @Query("UPDATE Stock s SET s.quantiteDisponible = :quantite, s.dateMiseAJour = CURRENT_TIMESTAMP WHERE s.nomIngredient = :nom")
    void updateStockByNom(@Param("nom") String nom, @Param("quantite") Integer quantite);
}