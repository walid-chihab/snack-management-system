package com.snack.repository;

import com.snack.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Cette interface est un Repository, elle sert à accéder à la base de données
public interface ClientRepository extends JpaRepository<Client, Long> {
        boolean existsByEmail(String email);  // ← AJOUTE CETTE LIGNE !

}