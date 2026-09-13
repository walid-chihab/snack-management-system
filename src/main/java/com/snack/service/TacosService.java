package com.snack.service;

import com.snack.model.Commande;
import java.util.List;

public interface CommandeService {
    
    // Créer une commande avec vérification de stock
    Commande creerCommande(Commande commande);
    
    // Récupérer toutes les commandes
    List<Commande> getAllCommandes();
    
    // Récupérer une commande par son ID
    Commande getCommandeById(Long id);
    
    // Récupérer les commandes d'un client
    List<Commande> getCommandesByClient(Long clientId);
    
    // Annuler une commande
    void annulerCommande(Long id);
    
    // Changer le statut d'une commande
    Commande changerStatut(Long id, String statut);
}