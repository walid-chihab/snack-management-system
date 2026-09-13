# Snack Management System

Un système de gestion de snacks développé avec Spring Boot 3.4.0.

## Configuration Technique

- **Spring Boot**: 3.4.0
- **Java**: 21
- **Build Tool**: Maven
- **Group ID**: com.snack
- **Artifact ID**: snack-management-system

## Dépendances

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- H2 Database (embedded)
- Lombok
- Spring Boot DevTools

## Prérequis

- Java 21 ou supérieur
- Maven 3.6.0 ou supérieur

## Démarrage

### Compilation

```bash
mvn clean install
```

### Exécution

```bash
mvn spring-boot:run
```

L'application démarrera sur `http://localhost:8080`

## Accès à la Console H2

La console H2 est accessible à l'adresse: `http://localhost:8080/h2-console`

- **URL JDBC**: `jdbc:h2:mem:snackdb`
- **Utilisateur**: `sa`
- **Mot de passe**: (laisser vide)

## Structure du Projet

```
snack-management-system/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── snack/
    │   │           └── SnackManagementSystemApplication.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── com/
                └── snack/
                    └── SnackManagementSystemApplicationTests.java
```

## Notes

- DevTools est configuré pour le rechargement automatique lors du développement
- La base de données H2 est une base de données en mémoire (réinitialisée au redémarrage)
