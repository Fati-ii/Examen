package com.riffo.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riffo.users.entity.Partenaire;
import com.riffo.users.service.PartenaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartenaireRESTController.class)
public class PartenaireRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartenaireService partenaireService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void getAllPartenaires_ReturnsList() throws Exception {
        when(partenaireService.getAllPartenaires()).thenReturn(Arrays.asList(partenaire));

        mockMvc.perform(get("/api/partenaires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Test Partner"));
    }

    @Test
    void getPartenaireById_ExistingId_ReturnsPartenaire() throws Exception {
        when(partenaireService.getPartenaireById(1L)).thenReturn(Optional.of(partenaire));

        mockMvc.perform(get("/api/partenaires/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Test Partner"));
    }

    @Test
    void addPartenaire_ValidData_ReturnsCreated() throws Exception {
        when(partenaireService.addPartenaire(any(Partenaire.class))).thenReturn(partenaire);

        mockMvc.perform(post("/api/partenaires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partenaire)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Test Partner"));
    }

    @Test
    void addPartenaire_InvalidData_ReturnsBadRequest() throws Exception {
        Partenaire invalidPartenaire = new Partenaire(); // Missing required fields

        mockMvc.perform(post("/api/partenaires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPartenaire)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPartenaireByNom_ExistingNom_ReturnsOk() throws Exception {
        when(partenaireService.getPartenaireByNom("Test Partner")).thenReturn(Optional.of(partenaire));
        mockMvc.perform(get("/api/partenaires/search/nom?nom=Test Partner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Test Partner"));
    }

    @Test
    void getPartenairesByCategorie_ReturnsOk() throws Exception {
        when(partenaireService.getPartenairesByCategorie("Assurance")).thenReturn(Arrays.asList(partenaire));
        mockMvc.perform(get("/api/partenaires/search/categorie?categorie=Assurance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categorie").value("Assurance"));
    }

    @Test
    void getPartenairesByStatut_ReturnsOk() throws Exception {
        when(partenaireService.getPartenairesByStatut("Actif")).thenReturn(Arrays.asList(partenaire));
        mockMvc.perform(get("/api/partenaires/search/statut?statut=Actif"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$ [0].statut").value("Actif"));
    }

    @Test
    void getPartenairesByVille_ReturnsOk() throws Exception {
        when(partenaireService.getPartenairesByVille("Test City")).thenReturn(Arrays.asList(partenaire));
        mockMvc.perform(get("/api/partenaires/search/ville?ville=Test City"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ville").value("Test City"));
    }

    @Test
    void getPartenaireByEmail_ExistingEmail_ReturnsOk() throws Exception {
        when(partenaireService.getPartenaireByEmail("test@partner.com")).thenReturn(Optional.of(partenaire));
        mockMvc.perform(get("/api/partenaires/search/email?email=test@partner.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@partner.com"));
    }

    @Test
    void updatePartenaire_ValidData_ReturnsOk() throws Exception {
        when(partenaireService.updatePartenaire(anyLong(), any(Partenaire.class))).thenReturn(partenaire);
        mockMvc.perform(put("/api/partenaires/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partenaire)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Test Partner"));
    }

    @Test
    void countPartenaires_ReturnsCount() throws Exception {
        when(partenaireService.countPartenaires()).thenReturn(5L);
        mockMvc.perform(get("/api/partenaires/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void existsByEmail_ReturnsBoolean() throws Exception {
        when(partenaireService.existsByEmail("test@partner.com")).thenReturn(true);
        mockMvc.perform(get("/api/partenaires/exists/email?email=test@partner.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deletePartenaire_Successful_ReturnsNoContent() throws Exception {
        doNothing().when(partenaireService).deletePartenaire(1L);

        mockMvc.perform(delete("/api/partenaires/1"))
                .andExpect(status().isNoContent());
    }
}
