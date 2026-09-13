package com.snack.service;

import com.snack.model.Produit;
import com.snack.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    // Injection via constructeur
    private final ProduitRepository produitRepository;

    // Constructeur pour l'injection de dépendance
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    // CREATE : Ajouter un produit
    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }

    // READ : Récupérer tous les produits
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    // READ : Récupérer un produit par son ID
    public Produit findById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
    }

    // UPDATE : Modifier un produit (CORRIGÉ)
    public Produit updateProduit(Long id, Produit produitDetails) {
        Produit produit = findById(id);
        produit.setNom(produitDetails.getNom());
        produit.setQuantite(produitDetails.getQuantite());  // ✅ CORRIGÉ
        produit.setSeuilAlerte(produitDetails.getSeuilAlerte());
        produit.setPrixVente(produitDetails.getPrixVente());
        return produitRepository.save(produit);
    }

    // UPDATE : Décrémenter le stock
    public void decrementerStock(Long id, int quantite) {
        Produit produit = findById(id);
        if (produit.getQuantite() < quantite) {
            throw new RuntimeException("❌ Stock insuffisant ! Disponible : " 
                    + produit.getQuantite() + " | Demandé : " + quantite);
        }
        produit.decrementer(quantite);
        produitRepository.save(produit);
        if (produit.estEnDessousSeuil()) {
            System.out.println("⚠️ ALERTE : Stock faible pour " + produit.getNom() 
                    + " (" + produit.getQuantite() + " restant)");
        }
    }

    // DELETE : Supprimer un produit
    public void deleteProduit(Long id) {
        Produit produit = findById(id);
        produitRepository.delete(produit);
    }

    // MÉTHODES MÉTIER : Produits en alerte
    public List<Produit> getProduitsEnAlerte() {
        return produitRepository.findAll().stream()
                .filter(Produit::estEnDessousSeuil)
                .toList();
    }
}