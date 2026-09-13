# Architecture du système Snack Management System

Ce document présente une vue claire et structurée, sous forme de tableaux 2D, de l’architecture du projet pour faciliter la compréhension par l’équipe technique et les chefs de projet.

---

## 1. Vue générale par couches

| Couche | Packages / Classes | Rôle principal | Relations principales |
|---|---|---|---|
| Application | `com.snack.SnackManagementSystemApplication` | Point d’entrée Spring Boot | Lance l’application |
| Contrôleur | `com.snack.controller.ClientController` | Expose les APIs REST | Appelle `ClientService` |
| Service métier | `ClientService`, `CommandeServiceImpl`, `ProduitService`, `StockServiceImpl` | Implémente la logique métier | Utilisent les repositories |
| Accès aux données | `ClientRepository`, `CommandeRepository`, `ProduitRepository`, `StockRepository`, `LigneCommandeRepository` | Interaction avec la base de données | Gèrent les entités JPA |
| Modèle | `Client`, `Commande`, `LigneCommande`, `Produit`, `Stock`, `Etage`, `Rayon`, `Fournisseur`, `Mega`, `Tacos`, `Partie` | Représentation des données du système | Sont manipulées par services et repositories |
| Enumérations | `StatutCommande`, `TypeCommande`, `TypeMega`, `TypeTacos` | Définissent les états et types | Utilisées par commandes et produits |

---

## 2. Tableau des classes et responsabilités

| Classe / Interface | Package | Description | Responsabilités principales |
|---|---|---|---|
| `SnackManagementSystemApplication` | `com.snack` | Classe principale | Démarre le projet Spring Boot |
| `ClientController` | `com.snack.controller` | API REST pour les clients | Ajouter, lister, récupérer, modifier et supprimer un client |
| `ClientService` | `com.snack.service` | Service métier client | Validation des données, vérification email, CRUD client |
| `ClientRepository` | `com.snack.repository` | Repository JPA client | Sauvegarde et recherche des clients |
| `Client` | `com.snack.model` | Entité client | Informations client : nom, prénom, email, téléphone, adresse |
| `CommandeService` | `com.snack.service` | Service abstrait de commande | Déclare les opérations du module commande |
| `CommandeServiceImpl` | `com.snack.service` | Implémentation du service commande | Créer commande, vérifier stock, annuler commande, changer statut |
| `CommandeRepository` | `com.snack.repository` | Repository JPA commande | Recherche commandes selon client, statut et type |
| `Commande` | `com.snack.model` | Entité commande | Représente une commande client |
| `LigneCommande` | `com.snack.model` | Détail d’une commande | Quantité et lien produit/commande |
| `LigneCommandeRepository` | `com.snack.repository` | Repository des lignes de commande | Recherche lignes selon commande ou produit |
| `ProduitService` | `com.snack.service` | Service produit | Gestion CRUD produit |
| `ProduitRepository` | `com.snack.repository` | Repository JPA produit | Persistance des produits |
| `Produit` | `com.snack.model` | Entité produit | Représente un produit du magasin |
| `StockService` | `com.snack.service` | Service stock | Contrat de gestion du stock |
| `StockServiceImpl` | `com.snack.service` | Implémentation gestion stock | Vérifier, réserver, libérer le stock |
| `StockRepository` | `com.snack.repository` | Repository stock | Accès aux données de stock |
| `Stock` | `com.snack.model` | Entité stock | Stock d’ingrédient et quantité disponible |
| `EtageRepository` | `com.snack.repository` | Repository étages | Recherche par numéro |
| `Etage` | `com.snack.model` | Entité étage | Niveau de stockage |
| `RayonRepository` | `com.snack.repository` | Repository rayons | Recherche des rayons par nom et étage |
| `Rayon` | `com.snack.model` | Entité rayon | Zone de vente ou stockage |
| `FournisseurRepository` | `com.snack.repository` | Repository fournisseur | Recherches selon nom et ville |
| `Fournisseur` | `com.snack.model` | Entité fournisseur | Données fournisseur |
| `MegaRepository` | `com.snack.repository` | Repository produits Mega | Recherches par type, prix et présence de frites |
| `Mega` | `com.snack.model` | Produit spécial Mega | Type et caractéristiques |
| `TacosRepository` | `com.snack.repository` | Repository produits Tacos | Recherches par type, prix et options |
| `Tacos` | `com.snack.model` | Produit spécial Tacos | Type et caractéristiques |
| `Partie` | `com.snack.model` | Entité complémentaire | Classe non encore reliée de façon détaillée |
| `StatutCommande` | `com.snack.model` | Enumération | Etats d’une commande |
| `TypeCommande` | `com.snack.model` | Enumération | Type de commande : sur place / à emporter |
| `TypeMega` | `com.snack.model` | Enumération | Types de Mega |
| `TypeTacos` | `com.snack.model` | Enumération | Types de Tacos |

---

## 3. Relations métier principales

| Entité source | Relation | Entité cible | Cardinalité |
|---|---|---|---|
| `Client` | passe | `Commande` | 1 client → plusieurs commandes |
| `Commande` | contient | `LigneCommande` | 1 commande → plusieurs lignes |
| `Produit` | apparaît dans | `LigneCommande` | 1 produit → plusieurs lignes |
| `Commande` | a un | `StatutCommande` | Plusieurs commandes peuvent avoir le même statut |
| `Etage` | contient | `Rayon` | 1 étage → plusieurs rayons |
| `Rayon` | appartient à | `Etage` | plusieurs rayons → 1 étage |
| `Stock` | sert à vérifier | `Produit` | 1 stock peut servir à plusieurs produits selon logique métier |
| `CommandeServiceImpl` | utilise | `StockService` | 1 service commande → 1 service stock |

---

## 4. Relations techniques (controller / service / repository)

| Composant | Dépend de | Nature de la dépendance |
|---|---|---|
| `ClientController` | `ClientService` | Injection de dépendance |
| `ClientService` | `ClientRepository` | Accès aux données |
| `CommandeServiceImpl` | `CommandeRepository` | Accès aux commandes |
| `CommandeServiceImpl` | `ProduitRepository` | Recherche des produits |
| `CommandeServiceImpl` | `StockService` | Vérification et réservation du stock |
| `ProduitService` | `ProduitRepository` | Accès aux produits |
| `StockServiceImpl` | `StockRepository` | Gestion du stock |
| `LigneCommandeRepository` | `Commande`, `Produit`, `LigneCommande` | Liaison avec les lignes de commande |

---

## 5. Description fonctionnelle du système

| Module | Fonction | Exemples |
|---|---|---|
| Gestion client | Ajouter, modifier, supprimer, lister les clients | `ClientController` |
| Gestion commande | Créer, annuler, changer le statut d’une commande | `CommandeServiceImpl` |
| Gestion stock | Vérifier, réserver, libérer le stock | `StockServiceImpl` |
| Gestion produit | CRUD produit | `ProduitService` |
| Gestion rayon / étage | Organisation du magasin | `Rayon`, `Etage` |
| Gestion fournisseur | Suivi des fournisseurs | `FournisseurRepository` |
| Gestion produits spécifiques | Mega et Tacos | `MegaRepository`, `TacosRepository` |

---

## 6. Représentation simplifiée du système

```text
ClientController
       │
       ▼
ClientService
       │
   +---+----------------------+
   │                           │
   ▼                           ▼
ClientRepository           CommandeServiceImpl
                              │
                              ├──> CommandeRepository
                              ├──> ProduitRepository
                              └──> StockServiceImpl
                                            │
                                            ▼
                                       StockRepository
```

---

## 7. Synthèse

Le système est organisé selon une architecture Spring Boot classique :

- Couche Controller : exposer les endpoints REST
- Couche Service : logique métier
- Couche Repository : accès aux données JPA
- Couche Model : entités et enums

Cette structure est cohérente avec les standards de grands projets logiciels et permet une maintenance, une évolutivité et une compréhension plus faciles.

---

## 8. Fichier source UML associé

Le diagramme UML complet se trouve dans le fichier :

- `snack-management-system.puml`

---

Cette vue en tableau 2D est pensée pour être claire, lisible et directement exploitable lors de revues de conception ou d’échanges avec des chefs d’équipe.
