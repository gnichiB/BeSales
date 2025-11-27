package com.beyondsales.beyondsales.controller;

import com.beyondsales.beyondsales.entity.Type;
import com.beyondsales.beyondsales.service.TypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/types")
public class TypeController {

    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    // === GET - TOUS LES TYPES ===
    @GetMapping
    public ResponseEntity<List<Type>> getAllTypes() {
        List<Type> types = typeService.findAll();
        return ResponseEntity.ok(types);
    }

    // === GET - TYPES ACTIFS ===
    @GetMapping("/active")
    public ResponseEntity<List<Type>> getActiveTypes() {
        List<Type> types = typeService.findActiveTypes();
        return ResponseEntity.ok(types);
    }

    // === GET - TYPE PAR CODE ===
    @GetMapping("/{typeCode}")
    public ResponseEntity<Type> getTypeByCode(@PathVariable String typeCode) {
        Optional<Type> type = typeService.findByTypeCode(typeCode);
        return type.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST - CRÉER UN TYPE ===
    @PostMapping
    public ResponseEntity<?> createType(@RequestBody Type type,
                                        @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Type savedType = typeService.createType(type, currentUser);
            return ResponseEntity.ok(savedType);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === PUT - METTRE À JOUR UN TYPE ===
    @PutMapping("/{typeCode}")
    public ResponseEntity<?> updateType(@PathVariable String typeCode,
                                        @RequestBody Type typeDetails,
                                        @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Type updatedType = typeService.updateType(typeCode, typeDetails, currentUser);
            return ResponseEntity.ok(updatedType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === DELETE - SUPPRIMER UN TYPE ===
    @DeleteMapping("/{typeCode}")
    public ResponseEntity<?> deleteType(@PathVariable String typeCode,
                                        @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            typeService.deleteType(typeCode, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === RECHERCHE PAR DESCRIPTION ===
    @GetMapping("/search/description")
    public ResponseEntity<List<Type>> searchByDescription(@RequestParam String keyword) {
        List<Type> types = typeService.findByTypeDescriptionContainingIgnoreCase(keyword);
        return ResponseEntity.ok(types);
    }

    // === RECHERCHE PAR CODE ===
    @GetMapping("/search/code")
    public ResponseEntity<List<Type>> searchByCode(@RequestParam String typeCode) {
        List<Type> types = typeService.findByTypeCodeContainingIgnoreCase(typeCode);
        return ResponseEntity.ok(types);
    }
}