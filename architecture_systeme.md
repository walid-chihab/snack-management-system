# Architecture du système — Snack Management System

Ce document présente une vue claire et structurée de l'architecture du projet, pour faciliter la compréhension par l'équipe technique et les chefs de projet.

---

## 1. Vue générale par couches

-------------------------------------------------------------------------------------------------------------------------------
|     Couche  |             Packages / Classes               |         Rôle principal          |    Relations principales      |
|-------------|----------------------------------------------|---------------------------------|-------------------------------|
| Application | `com.snack.SnackManagementSystemApplication` |     Point d'entrée Spring Boot  |   Lance l'application         |
|  Contrôleur |     `com.snack.controller.*`                 |     Expose les APIs REST        |   Appellent les services      |
|   Service   |     `com.snack.service.*`                    |         Logique métier          |   Utilisent les repositories  |
|  Repository |     `com.snack.repository.*`                 |         Accès aux données       |   Gèrent les entités JPA      |
|   Modèle    |     `com.snack.model.*`                      |         Entités JPA             |   Mappées aux tables          |
|   Enums     |     `com.snack.model.enums.*`                |         États et types          |  Utilisés par les entités     |
|    DTO      |     `com.snack.dto.*`                        |      Objets de transfert        |   Entrée / sortie API         |
|   Mapper    |     `com.snack.mapper.*`                     |     Conversion Entity ↔ DTO     |   Utilisés par les services   |
|  Exception  |     `com.snack.exception.*`                  | Gestion centralisée des erreurs |   Intercepte les exceptions   |
|   Config    |     `com.snack.config.*`                     |        Configuration Spring     |   CORS, OpenAPI, Jackson      |
--------------------------------------------------------------------------------------------------------------------------------

## 2. Tableau des classes et responsabilités

----------------------------------------------------------------------------------------------------------------------------------------------
|                Class             |     Package   |          Description          |                     Responsabilités                      |
|---------- -----------------------|---------------|-------------------------------|----------------------------------------------------------|
|`SnackManagementSystemApplication'|   `com.snack` |        Classe principale      |                 Démarre Spring Boot                      |
|          `Client`                |     `model`   |        Entité client          |          nom, prénom, email, téléphone, adresse          |
|                `Commande`        |     `model`   |        Entité commande        |        date, statut, type, montant total, client         |
|         `LigneCommande`          |     `model    |      Ligne d'une commande     | quantité, prix unitaire, instructions, produit, commande |
|             `Produit`            |   `model`     |        Entité produit         |       nom, prix base, disponible, type, taille, volume   |
|             `Stock`              |      `model`  |      Ingrédient en stock      |       nom ingrédient, quantité, seuil d'alerte           |
|           `Supplement`           |      `model`  |     Supplément ajoutable      |             nom, prix, stock associé                     |
|          `Paiement`              |       `model` |     Paiement d'une commande   |         montant, mode, est payé, date                    |
|          `IngredientRetire`      |      `model`  | Ingrédient retiré d'une ligne |                    ligne, stock                          |
|       `LigneSupplement`          |      `model`  | Supplément ajouté à une ligne |         ligne, supplément, quantité                      |
|        `StatutCommande`          | `model.enums` |           Enum                |         EN_ATTENTE, EN_COURS, LIVREE, ANNULEE            |
|       `TypeCommande`             | `model.enums` |           Enum                |         SUR_PLACE, A_EMPORTER, LIVRAISON                 |
|        `ModePaiement`            | `model.enums` |           Enum                |          CARTE, ESPECES, TICKET_RESTO                    |
|       `ClientRepository`         |  `repository` |         Repository JPA        |                CRUD clients                              |
|      `CommandeRepository`        |  `repository` |         Repository JPA        |                CRUD commandes                            |
|    `LigneCommandeRepository`     |  `repository` |         Repository JPA        |                CRUD lignes de commande                   |
|        `ProduitRepository`       |  `repository` |         Repository JPA        |                CRUD produits                             |
|       `StockRepository`          |  `repository` |         Repository JPA        |                CRUD stocks                               |
|     `SupplementRepository`       |  `repository` |         Repository JPA        |                CRUD suppléments                          |
|      `PaiementRepository`        |  `repository` |         Repository JPA        |                 CRUD paiements                           |
|       `ClientService`            |    `service`  |        Service client         |            CRUD + validation email                       |
|      `CommandeService`           |   `service`   |        Service commande       |           Créer, annuler, changer statut                 |
|      `ProduitService`            |   `service`   |        Service produit        |                   CRUD produits                          |
|      `StockService`              |   `service`   |        Service stock          |             Vérifier, réserver, libérer                  |
|      `SupplementService`         |   `service`   |        Service supplément     |                 CRUD suppléments                         |
|       `PaiementService`          |   `service`   |        Service paiement       |             Enregistrer, valider paiement                |
|      `ClientController`          |  `controller` |       API REST client         |                       /api/clients                       |
|       `CommandeController`       |  `controller` |       API REST commande       |                       /api/commandes                     |
|       `ProduitController`        |  `controller` |       API REST produit        |                       /api/produits                      |
|       `StockController`          |  `controller` |       API REST stock          |                        /api/stocks                       |
|      `SupplementController`      |  `controller` |        API REST supplément    |                       /api/supplements                   |
|      `PaiementController`        |  `controller` |       API REST paiement       |                        /api/paiements                    |
-__________________________________-_______________-_______________________________-__________________________________________________________-

## 3. Relations métier principales

| Entité source | Relation | Entité cible | Cardinalité |
|---|---|---|---|
| `Client` | passe | `Commande` | 1 client → N commandes |
| `Commande` | contient | `LigneCommande` | 1 commande → N lignes |
| `Commande` | possède | `Paiement` | 1 commande → 1 paiement |
| `Produit` | apparaît dans | `LigneCommande` | 1 produit → N lignes |
| `Produit` | utilise | `Stock` | N produits ↔ N stocks (via `produit_ingredient`) |
| `LigneCommande` | retire | `Stock` | N lignes ↔ N stocks (via `ingredient_retire`) |
| `LigneCommande` | ajoute | `Supplement` | N lignes ↔ N suppléments (via `ligne_supplement`) |
| `Supplement` | dépend de | `Stock` | 1 supplément → 1 stock |

---

## 4. Relations techniques (Controller → Service → Repository)

| Composant | Dépend de | Nature |
|---|---|---|
| `ClientController` | `ClientService` | Injection de dépendance |
| `CommandeController` | `CommandeService` | Injection de dépendance |
| `ProduitController` | `ProduitService` | Injection de dépendance |
| `StockController` | `StockService` | Injection de dépendance |
| `SupplementController` | `SupplementService` | Injection de dépendance |
| `PaiementController` | `PaiementService` | Injection de dépendance |
| `ClientService` | `ClientRepository` | Accès aux données |
| `CommandeService` | `CommandeRepository`, `LigneCommandeRepository`, `StockService` | Accès + logique |
| `ProduitService` | `ProduitRepository`, `StockRepository` | Accès aux données |
| `StockService` | `StockRepository` | Accès aux données |
| `SupplementService` | `SupplementRepository`, `StockRepository` | Accès aux données |
| `PaiementService` | `PaiementRepository`, `CommandeRepository` | Accès aux données |

---

## 5. Description fonctionnelle

| Module | Fonction | Exemples |
|---|---|---|
| Gestion client | Ajouter, modifier, supprimer, lister | `ClientController` |
| Gestion commande | Créer, annuler, changer le statut | `CommandeService` |
| Gestion produit | CRUD produit | `ProduitService` |
| Gestion stock | Vérifier, réserver, libérer | `StockService` |
| Gestion supplément | Ajouter des suppléments à une ligne | `SupplementService` |
| Gestion paiement | Enregistrer, valider un paiement | `PaiementService` |
---------------------------------------------------------------------------------------------------
## 6. Représentation simplifiée

ClientController
       │
       ▼
ClientService ──> ClientRepository
       │
       ▼
CommandeService ──> CommandeRepository
       │           ├──> LigneCommandeRepository
       │           └──> StockService ──> StockRepository
       │
       ▼
PaiementService ──> PaiementRepository