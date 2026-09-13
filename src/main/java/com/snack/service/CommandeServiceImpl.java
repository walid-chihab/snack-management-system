package com.snack.service;

import com.snack.model.Commande;
import com.snack.model.LigneCommande;
import com.snack.model.Produit;
import com.snack.model.StatutCommande;
import com.snack.repository.CommandeRepository;
import com.snack.repository.LigneCommandeRepository;
import com.snack.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service  
@Transactional 
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final StockService stockService;

    public CommandeServiceImpl(CommandeRepository commandeRepository, ProduitRepository produitRepository,StockService stockService) {
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
        this.stockService = stockService;
    }

    @Override
    public Commande creerCommande(Commande commande) {
        // 1. Vérifier le stock pour chaque produit
        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = produitRepository.findById(ligne.getProduit().getId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
            
            // Vérifier le stock (à implémenter avec les ingrédients)
            stockService.verifierStockPourProduit(produit, ligne.getQuantite());
        }
        
        // 2. Réserver le stock
        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = produitRepository.findById(ligne.getProduit().getId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
            
            stockService.reserverStockPourProduit(produit, ligne.getQuantite());
        }
        
        // 3. Créer la commande
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut(StatutCommande.EN_ATTENTE);
        
        return commandeRepository.save(commande);
    }

    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Override
    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Commande non trouvée avec ID: " + id));
    }

    @Override
    public List<Commande> getCommandesByClient(Long clientId) {
        return commandeRepository.findByClientId(clientId);
    }

    @Override
    public void annulerCommande(Long id) {
        Commande commande = getCommandeById(id);
        
        // Libérer le stock
        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = ligne.getProduit();
            stockService.libererStockPourProduit(produit, ligne.getQuantite());
        }
        
        commande.setStatut(StatutCommande.ANNULEE);
        commandeRepository.save(commande);
    }

    @Override
    public Commande changerStatut(Long id, String statut) {
        Commande commande = getCommandeById(id);
        commande.setStatut(StatutCommande.valueOf(statut));
        return commandeRepository.save(commande);
    }
}