# Rapport d'Analyse et d'Amélioration - Plateforme Partenaire

## 1. Défauts de Qualité Identifiés (Partie 1)

Cinq défauts majeurs ont été identifiés dans le code source initial :

1.  **Absence de Validation des Entités (Validation)** : L'entité `Partenaire` ne possédait aucune annotation de contrainte (`@NotBlank`, `@Email`, etc.).
    *   *Problème* : Risque d'incohérence et de corruption des données en base.
2.  **Champs avec Caractères Accentués (Conception/Portabilité)** : Les variables `catégorie` et `téléphone` utilisaient des accents.
    *   *Problème* : Risques de problèmes d'encodage et de compilation selon l'environnement.
3.  **Gestion des Erreurs Minimaliste (Robustesse)** : Utilisation de `try-catch` génériques dans le contrôleur avec des retours 400/404 sans messages explicites.
    *   *Problème* : Manque de clarté pour les clients de l'API sur les causes d'échec.
4.  **Dépendances Manquantes (Robustesse)** : Le fichier `pom.xml` ne contenait pas les starters essentiels comme `spring-boot-starter-validation` et `spring-boot-starter-web`.
    *   *Problème* : Le projet ne pouvait pas compiler avec les annotations de validation ou fonctionner comme une API REST complète.
5.  **Architecture non respectée (Conception)** : Fuite de la logique de gestion d'erreurs métier dans la couche contrôleur au lieu d'une gestion globale centralisée.

## 2. Corrections Apportées (Partie 2)

*   **Renommage des variables** : `catégorie` -> `categorie`, `téléphone` -> `telephone`.
*   **Validation Java Validation** : Ajout des annotations Apache Hibernate Validator sur l'entité `Partenaire` et utilisation de `@Valid` dans le contrôleur.
*   **Exceptions Personnalisées** : Création de `ResourceNotFoundException` et `GlobalExceptionHandler` pour une gestion propre et centralisée des erreurs REST.
*   **Refactorisation du Contrôleur** : Suppression des blocs `try-catch` redondants pour déléguer la gestion des erreurs au handler global.

## 3. Résultats des Tests et Couverture (Partie 3 & 4)

### Tests Unitaires (Service)
*   Tests écrits avec Mockito pour la couche `PartenaireServiceImpl`.
*   Scénarios testés : création réussie, recherche par ID (existant/inexistant), gestion des erreurs.
*   **Résultat** : Tous les tests passent.

### Tests d'Intégration (Controller)
*   Tests écrits avec MockMvc pour les endpoints `POST`, `GET`, et `DELETE`.
*   Vérification des codes de retour (200, 201, 204, 400, 404).
*   **Résultat** : Tous les tests passent.

### Couverture de Code (JaCoCo)
*   Le rapport JaCoCo a été généré via `mvn jacoco:report`.
*   Toutes les classes métier (`Partenaire`, `PartenaireService`, `PartenaireRESTController`) sont couvertes par les tests.

## 4. Industrialisation (Partie 5)

*   **Checkstyle** : Intégration de Checkstyle avec la configuration Google.
*   **GitHub Actions** : Mise en place d'un pipeline `.github/workflows/main.yml` automatisé qui compile, exécute les tests (avec base H2) et génère les rapports de qualité à chaque push.
