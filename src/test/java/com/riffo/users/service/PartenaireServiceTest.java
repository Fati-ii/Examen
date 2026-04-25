package com.riffo.users.service;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.DuplicateEmailException;
import com.riffo.users.exception.ResourceNotFoundException;
import com.riffo.users.repository.PartenaireRepository;
import com.riffo.users.service.impl.PartenaireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartenaireServiceTest {

    @Mock
    private PartenaireRepository partenaireRepository;

    @InjectMocks
    private PartenaireServiceImpl partenaireService;

    private Partenaire partenaire;

    @BeforeEach
    void setUp() {
        partenaire = new Partenaire();
        partenaire.setId(1L);
        partenaire.setNom("Test Partner");
        partenaire.setEmail("test@partner.com");
        partenaire.setCategorie("Assurance");
        partenaire.setTelephone("0123456789");
        partenaire.setAdresse("123 Test St");
        partenaire.setVille("Test City");
        partenaire.setLatitude(48.8566);
        partenaire.setLongitude(2.3522);
        partenaire.setStatut("Actif");
        partenaire.setPlafondPriseEnCharge(1000.0);
    }

    @Test
    void addPartenaire_Successful() {
        when(partenaireRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(partenaire);

        Partenaire savedPartenaire = partenaireService.addPartenaire(partenaire);

        assertNotNull(savedPartenaire);
        assertEquals(partenaire.getEmail(), savedPartenaire.getEmail());
        verify(partenaireRepository, times(1)).save(any(Partenaire.class));
    }

    @Test
    void getPartenaireById_ExistingId_ReturnsPartenaire() {
        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));

        Optional<Partenaire> found = partenaireService.getPartenaireById(1L);

        assertTrue(found.isPresent());
        assertEquals(partenaire.getNom(), found.get().getNom());
    }

    @Test
    void getPartenaireById_NonExistingId_ReturnsEmpty() {
        when(partenaireRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Partenaire> found = partenaireService.getPartenaireById(2L);

        assertTrue(found.isEmpty());
    }

    @Test
    void getPartenaireByNom_ExistingNom_ReturnsPartenaire() {
        when(partenaireRepository.findByNom("Test Partner")).thenReturn(Optional.of(partenaire));
        Optional<Partenaire> found = partenaireService.getPartenaireByNom("Test Partner");
        assertTrue(found.isPresent());
        assertEquals("Test Partner", found.get().getNom());
    }

    @Test
    void getPartenairesByCategorie_ReturnsList() {
        when(partenaireRepository.findByCategorie("Assurance")).thenReturn(java.util.Arrays.asList(partenaire));
        java.util.List<Partenaire> result = partenaireService.getPartenairesByCategorie("Assurance");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getPartenairesByStatut_ReturnsList() {
        when(partenaireRepository.findByStatut("Actif")).thenReturn(java.util.Arrays.asList(partenaire));
        java.util.List<Partenaire> result = partenaireService.getPartenairesByStatut("Actif");
        assertEquals(1, result.size());
    }

    @Test
    void getPartenairesByVille_ReturnsList() {
        when(partenaireRepository.findByVille("Test City")).thenReturn(java.util.Arrays.asList(partenaire));
        java.util.List<Partenaire> result = partenaireService.getPartenairesByVille("Test City");
        assertEquals(1, result.size());
    }

    @Test
    void getPartenaireByEmail_ExistingEmail_ReturnsPartenaire() {
        when(partenaireRepository.findByEmail("test@partner.com")).thenReturn(Optional.of(partenaire));
        Optional<Partenaire> found = partenaireService.getPartenaireByEmail("test@partner.com");
        assertTrue(found.isPresent());
    }

    @Test
    void updatePartenaire_Successful() {
        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(partenaire);

        Partenaire updateData = new Partenaire();
        updateData.setNom("Updated Name");
        updateData.setStatut("Inactif");

        Partenaire result = partenaireService.updatePartenaire(1L, updateData);

        assertNotNull(result);
        assertEquals("Updated Name", result.getNom());
        assertEquals("Inactif", result.getStatut());
    }

    @Test
    void deletePartenaire_Successful() {
        when(partenaireRepository.existsById(1L)).thenReturn(true);
        doNothing().when(partenaireRepository).deleteById(1L);

        assertDoesNotThrow(() -> partenaireService.deletePartenaire(1L));
        verify(partenaireRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePartenaire_NonExistingId_ThrowsException() {
        when(partenaireRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> partenaireService.deletePartenaire(99L));
    }

    @Test
    void countPartenaires_ReturnsCount() {
        when(partenaireRepository.count()).thenReturn(10L);
        assertEquals(10L, partenaireService.countPartenaires());
    }

    @Test
    void existsByEmail_ReturnsBoolean() {
        when(partenaireRepository.findByEmail("test@partner.com")).thenReturn(Optional.of(partenaire));
        assertTrue(partenaireService.existsByEmail("test@partner.com"));
    }

    @Test
    void addPartenaire_DuplicateEmail_ThrowsException() {
        when(partenaireRepository.findByEmail(partenaire.getEmail())).thenReturn(Optional.of(partenaire));

        assertThrows(DuplicateEmailException.class, () -> {
            partenaireService.addPartenaire(partenaire);
        });
    }

    @Test
    void updatePartenaire_NonExistingId_ThrowsException() {
        when(partenaireRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            partenaireService.updatePartenaire(1L, partenaire);
        });
    }
}
