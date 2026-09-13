package com.snack.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity // Cette classe est une TABLE dans la base de données
@Data // generer Constructeur avec paramètres,getters,setters,equals,hashCode,toString
public class Client {
    @Id //cle primaire du table Client
    @GeneratedValue(strategy = GenerationType.IDENTITY) // l'ID est généré automatiquement par la base de données
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
}