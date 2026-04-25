package com.riffo.users.repository;

import com.riffo.users.entity.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Partenaire
 */
@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {

    /**
     * Recherche un partenaire par son nom
     * @param nom le nom du partenaire
     * @return un Optional contenant le partenaire s'il existe
     */
    Optional<Partenaire> findByNom(String nom);

    /**
     * Recherche tous les partenaires par catégorie
     * @param categorie la catégorie du partenaire
     * @return une liste de partenaires
     */
    List<Partenaire> findByCategorie(String categorie);

    /**
     * Recherche tous les partenaires par statut
     * @param statut le statut du partenaire
     * @return une liste de partenaires
     */
    List<Partenaire> findByStatut(String statut);

    /**
     * Recherche tous les partenaires par ville
     * @param ville la ville du partenaire
     * @return une liste de partenaires
     */
    List<Partenaire> findByVille(String ville);

    /**
     * Recherche un partenaire par son email
     * @param email l'email du partenaire
     * @return un Optional contenant le partenaire s'il existe
     */
    Optional<Partenaire> findByEmail(String email);

    /**
     * Recherche tous les partenaires avec un statut particulier (insensible à la casse)
     * @param statut le statut
     * @return une liste de partenaires
     */
    List<Partenaire> findByStatutIgnoreCase(String statut);
}
