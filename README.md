# Snack Management System

> API REST de gestion de snack — Spring Boot 3.4 · Java 21 · JPA · H2

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue?logo=apachemaven)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/tests-22%20passed-success)](#tests)

---

## Présentation

**Snack Management System** est une API REST backend développée avec **Spring Boot 3.4** et **Java 21**. Elle permet de gérer les stocks, produits, commandes et employés d'un snack.

Le projet suit une **architecture en couches** (Controller → Service → Repository) avec une séparation stricte des responsabilités, une **gestion centralisée des erreurs** et une **couverture de tests complète**.

---

## Stack technique

| Catégorie | Technologie |
|-----------|-------------|
| Langage | Java 21 |
| Framework | Spring Boot 3.4.0 |
| Persistance | Spring Data JPA · Hibernate |
| Base de données | H2 (embedded) |
| Build | Maven |
| Tests | JUnit 5 · Mockito · MockMvc |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Utilitaires | Lombok · DevTools |
| Validation | Jakarta Bean Validation |

---

## Fonctionnalités

- CRUD complet sur les stocks
- Validation des entrées avec Jakarta Bean Validation
- Gestion centralisée des exceptions (`@RestControllerAdvice`)
- Documentation interactive via Swagger UI
- Tests automatisés : 22 tests (unitaire, intégration, web)
- Architecture en couches testable et maintenable

---

## Démarrage rapide

### Prérequis

- Java 21+
- Maven 3.6+

### Installation et exécution

```bash
# Cloner le projet
git clone https://github.com/tonpseudo/snack-management-system.git
cd snack-management-system

# Compiler
mvn clean install

# Lancer l'application
mvn spring-boot:run