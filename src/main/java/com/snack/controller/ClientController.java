package com.snack.controller; // 

import com.snack.model.Client;
import com.snack.service.ClientService;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController // Cette classe est un CONTROLLER REST pour gérer les clients
@RequestMapping("/api/clients") // Le chemin de base pour toutes les requêtes liées aux clients
public class ClientController {

    private final ClientService clientService; 

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping // Cette méthode gère les requêtes POST pour ajouter un nouveau client
    public ResponseEntity<?> ajouterClient(@RequestBody Client client) { 
        //ResponseEntity<?>	          : Retourne une réponse HTTP complète (status + body)
        //@RequestBody Client client  : Récupère le JSON envoyé et le transforme en objet Client
        try {
            Client nouveau = clientService.ajouterClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

 
    @GetMapping // Cette méthode gère les requêtes GET pour récupérer tous les clients
    public ResponseEntity<?> getClients() {
        try { 
            List<Client> clients = clientService.getClients();
            
            if (clients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Aucun client trouvé");
            }
            
            return ResponseEntity.ok(clients);
            
        } catch (DataAccessException e) {
            // 1- LIGNE Base hors ligne	SERVICE_UNAVAILABLE	' 503 ' Le service est indisponible
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Service indisponible. Vérifiez la connexion à la base de données.");
                    
        } catch (SQLException e) {
            //2- ERREUR SQL:  INTERNAL_SERVER_ERROR '500' Problème interne
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur technique lors de l'accès aux données.");
                    
        } catch (TimeoutException e) {
            //3- TIMEOUT :	REQUEST_TIMEOUT	'408'	La requête a pris trop de temps

            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("La requête a pris trop de temps. Veuillez réessayer.");
                    
        } catch (Exception e) {
            //AUTRES ERREURS (fallback)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inattendue : " + e.getMessage());
        }
    }


    @GetMapping("/{id}") // Cette méthode gère les requêtes GET pour récupérer un client par son ID
    public ResponseEntity<?> getClient(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clientService.getClient(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> modifierClient(@PathVariable Long id, @RequestBody Client client) {
        try {
            return ResponseEntity.ok(clientService.modifierClient(id, client));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerClient(@PathVariable Long id) {
        try {
            clientService.supprimerClient(id);
            return ResponseEntity.ok("Client supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } 
    }
}