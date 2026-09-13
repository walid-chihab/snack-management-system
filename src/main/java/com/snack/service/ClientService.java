package com.snack.service;

import com.snack.model.Client;
import com.snack.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    // Constructeur pour créer un objet ClientService avec un repository de clients
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Ajouter un nouveau client à la base de données
    public Client ajouterClient(Client client) {
        if (client.getEmail() == null || client.getEmail().isEmpty()) {
            throw new RuntimeException("L'email est obligatoire");
        }
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        return clientRepository.save(client);
    }

    // Récupérer un client par son ID (NOUVEAU - CORRIGÉ)
    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec ID: " + id));
    }

    // Récupérer tous les clients
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    // Modifier un client
    public Client modifierClient(Long id, Client newClient) {
        Client existing = getClient(id);
        if (!existing.getEmail().equals(newClient.getEmail()) 
                && clientRepository.existsByEmail(newClient.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        existing.setNom(newClient.getNom());
        existing.setPrenom(newClient.getPrenom());
        existing.setEmail(newClient.getEmail());
        existing.setTelephone(newClient.getTelephone());
        existing.setAdresse(newClient.getAdresse());
        return clientRepository.save(existing);
    }

    // Supprimer un client
    public void supprimerClient(Long id) {
        Client existing = getClient(id);
        clientRepository.delete(existing);
    }
}