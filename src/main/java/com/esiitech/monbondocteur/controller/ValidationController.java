package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.model.Validation;
import com.esiitech.monbondocteur.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/validations")
public class ValidationController {

    private final ValidationService validationService;

    // Injection du service ValidationService
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    // Route pour récupérer une validation par son code
    @GetMapping("/{code}")
    public Validation getValidationByCode(@PathVariable String code) {
        return validationService.lireEnFonctionDuCode(code);
    }

    // Route pour récupérer toutes les validations
    @GetMapping
    public List<Validation> getAllValidations() {
        return validationService.getValidationRipository().findAll();
    }

    // Route pour supprimer une validation par son code
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // HTTP 204 : suppression réussie
    public void deleteValidationById(@PathVariable Long id) {
        validationService.getValidationRipository().deleteById(id);
    }

}
