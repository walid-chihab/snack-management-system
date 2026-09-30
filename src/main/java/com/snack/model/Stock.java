package com.snack.model; 

import jakarta.persistence.*;
import lombok.*; 

@Entity
@Table(name = "stocks") // Nom de la table dans la base de données
@Getter // Génère les getters pour tous les champs
@Setter // Génère les setters pour tous les champs
@NoArgsConstructor // Génère un constructeur sans arguments 
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder // Génère un constructeur de type "builder" pour faciliter la création d'instances
public class Stock { // Nom de la classe représentant l'entité "Stock" dans la base de données

     
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Spécifie que la valeur de l'identifiant est générée automatiquement par 'db'
    @Column(name = "id") // Nom de la colonne dans la base de données
    private Long id; // Identifiant unique pour chaque enregistrement de stock

    @Column(name = "nom_ingredient", nullable = false, length = 100) // Nom de la colonne dans 'db'
    private String nomIngredient; 

    @Column(name = "quantite_en_stock", nullable = false)
    private Integer quantiteEnStock;

    @Column(name = "seuil_alerte", nullable = false)
    private Integer seuilAlerte;
    

}