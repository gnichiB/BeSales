package com.beyondsales.beyondsales.controller;

import com.beyondsales.beyondsales.entity.Family;
import com.beyondsales.beyondsales.service.FamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/families")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    // === GET - TOUTES LES FAMILLES ===
    @GetMapping
    public ResponseEntity<List<Family>> getAllFamilies() {
        List<Family> families = familyService.findAll();
        return ResponseEntity.ok(families);
    }

    // === GET - FAMILLES ACTIVES ===
    @GetMapping("/active")
    public ResponseEntity<List<Family>> getActiveFamilies() {
        List<Family> families = familyService.findActiveFamilies();
        return ResponseEntity.ok(families);
    }

    // === GET - FAMILLE PAR CODE ===
    @GetMapping("/{familyCode}")
    public ResponseEntity<Family> getFamilyByCode(@PathVariable String familyCode) {
        Optional<Family> family = familyService.findByFamilyCode(familyCode);
        return family.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST - CRÉER UNE FAMILLE ===
    @PostMapping
    public ResponseEntity<?> createFamily(@RequestBody Family family,
                                          @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Family savedFamily = familyService.createFamily(family, currentUser);
            return ResponseEntity.ok(savedFamily);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === PUT - METTRE À JOUR UNE FAMILLE ===
    @PutMapping("/{familyCode}")
    public ResponseEntity<?> updateFamily(@PathVariable String familyCode,
                                          @RequestBody Family familyDetails,
                                          @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Family updatedFamily = familyService.updateFamily(familyCode, familyDetails, currentUser);
            return ResponseEntity.ok(updatedFamily);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === DELETE - SUPPRIMER UNE FAMILLE ===
    @DeleteMapping("/{familyCode}")
    public ResponseEntity<?> deleteFamily(@PathVariable String familyCode,
                                          @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            familyService.deleteFamily(familyCode, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === RECHERCHE PAR DESCRIPTION ===
    @GetMapping("/search")
    public ResponseEntity<List<Family>> searchFamilies(@RequestParam String keyword) {
        List<Family> families = familyService.findByFamilyDescriptionContainingIgnoreCase(keyword);
        return ResponseEntity.ok(families);
    }
}