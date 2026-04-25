package com.riffo.users.controller;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.service.PartenaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des partenaires
 * Base URL: /api/partenaires
 */
@RestController
@RequestMapping("/api/partenaires")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PartenaireRESTController {

    private final PartenaireService partenaireService;

    @Autowired
    public PartenaireRESTController(PartenaireService partenaireService) {
        this.partenaireService = partenaireService;
    }

    /**
     * Récupère tous les partenaires
     * GET /api/partenaires
     * @return liste de tous les partenaires avec code 200
     */
    @GetMapping
    public ResponseEntity<List<Partenaire>> getAllPartenaires() {
        List<Partenaire> partenaires = partenaireService.getAllPartenaires();
        return ResponseEntity.ok(partenaires);
    }

    /**
     * Récupère un partenaire par son ID
     * GET /api/partenaires/{id}
     * @param id l'ID du partenaire
     * @return le partenaire avec code 200, ou 404 s'il n'existe pas
     */
    @GetMapping("/{id}")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        return partenaireService.getPartenaireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un partenaire par son nom
     * GET /api/partenaires/search/nom?nom={nom}
     * @param nom le nom du partenaire
     * @return le partenaire avec code 200, ou 404 s'il n'existe pas
     */
    @GetMapping("/search/nom")
    public ResponseEntity<Partenaire> getPartenaireByNom(@RequestParam String nom) {
        return partenaireService.getPartenaireByNom(nom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les partenaires d'une catégorie
     * GET /api/partenaires/search/categorie?categorie={categorie}
     * @param categorie la catégorie
     * @return liste des partenaires de la catégorie
     */
    @GetMapping("/search/categorie")
    public ResponseEntity<List<Partenaire>> getPartenairesByCategorie(@RequestParam String categorie) {
        List<Partenaire> partenaires = partenaireService.getPartenairesByCategorie(categorie);
        return ResponseEntity.ok(partenaires);
    }

    /**
     * Récupère tous les partenaires d'un statut
     * GET /api/partenaires/search/statut?statut={statut}
     * @param statut le statut
     * @return liste des partenaires du statut
     */
    @GetMapping("/search/statut")
    public ResponseEntity<List<Partenaire>> getPartenairesByStatut(@RequestParam String statut) {
        List<Partenaire> partenaires = partenaireService.getPartenairesByStatut(statut);
        return ResponseEntity.ok(partenaires);
    }

    /**
     * Récupère tous les partenaires d'une ville
     * GET /api/partenaires/search/ville?ville={ville}
     * @param ville la ville
     * @return liste des partenaires de la ville
     */
    @GetMapping("/search/ville")
    public ResponseEntity<List<Partenaire>> getPartenairesByVille(@RequestParam String ville) {
        List<Partenaire> partenaires = partenaireService.getPartenairesByVille(ville);
        return ResponseEntity.ok(partenaires);
    }

    /**
     * Récupère un partenaire par son email
     * GET /api/partenaires/search/email?email={email}
     * @param email l'email du partenaire
     * @return le partenaire avec code 200, ou 404 s'il n'existe pas
     */
    @GetMapping("/search/email")
    public ResponseEntity<Partenaire> getPartenaireByEmail(@RequestParam String email) {
        return partenaireService.getPartenaireByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Ajoute un nouveau partenaire
     * POST /api/partenaires
     * @param partenaire le partenaire à ajouter
     * @return le partenaire enregistré avec code 201
     */
    @PostMapping
    public ResponseEntity<Partenaire> addPartenaire(@Valid @RequestBody Partenaire partenaire) {
        Partenaire nouveauPartenaire = partenaireService.addPartenaire(partenaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauPartenaire);
    }

    /**
     * Met à jour un partenaire
     * PUT /api/partenaires/{id}
     * @param id l'ID du partenaire
     * @param partenaire les données mises à jour
     * @return le partenaire mis à jour avec code 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(@PathVariable Long id, @Valid @RequestBody Partenaire partenaire) {
        Partenaire partenaireMAJ = partenaireService.updatePartenaire(id, partenaire);
        return ResponseEntity.ok(partenaireMAJ);
    }

    /**
     * Supprime un partenaire
     * DELETE /api/partenaires/{id}
     * @param id l'ID du partenaire à supprimer
     * @return code 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        partenaireService.deletePartenaire(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Compte le nombre total de partenaires
     * GET /api/partenaires/count
     * @return le nombre de partenaires
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartenaires() {
        long count = partenaireService.countPartenaires();
        return ResponseEntity.ok(count);
    }

    /**
     * Vérifie si un partenaire existe par son email
     * GET /api/partenaires/exists/email?email={email}
     * @param email l'email du partenaire
     * @return true si le partenaire existe, false sinon
     */
    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        boolean exists = partenaireService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
