package com.snack.repository;

import com.snack.model.Commande;
import com.snack.model.StatutCommande;
import com.snack.model.TypeCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // Récupérer les commandes d'un client
    List<Commande> findByClientId(Long clientId);

    // Récupérer les commandes par statut
    List<Commande> findByStatut(StatutCommande statut);

    // NOUVEAU : Récupérer les commandes par type (sur place / à emporter)
    List<Commande> findByTypeCommande(TypeCommande typeCommande);

    // NOUVEAU : Récupérer les commandes par statut et type
    List<Commande> findByStatutAndTypeCommande(StatutCommande statut, TypeCommande typeCommande);
}