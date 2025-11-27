package com.beyondsales.beyondsales.controller;

import com.beyondsales.beyondsales.entity.Unit;
import com.beyondsales.beyondsales.service.UnitService;
import com.beyondsales.beyondsales.dto.UnitDTO;
import com.beyondsales.beyondsales.dto.CreateUnitRequest;
import com.beyondsales.beyondsales.dto.UpdateUnitRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    // === GET - LISTER TOUTES LES UNITÉS ===
    @GetMapping
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        List<Unit> units = unitService.findAll();
        List<UnitDTO> unitDTOs = units.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unitDTOs);
    }

    // === GET - UNITÉS ACTIVES ===
    @GetMapping("/active")
    public ResponseEntity<List<UnitDTO>> getActiveUnits() {
        List<Unit> units = unitService.findByActiveTrue();
        List<UnitDTO> unitDTOs = units.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unitDTOs);
    }

    // === GET - UNITÉ PAR CODE ===
    @GetMapping("/{unitCode}")
    public ResponseEntity<UnitDTO> getUnitByCode(@PathVariable String unitCode) {
        return unitService.findByUnitCode(unitCode)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET - RECHERCHE D'UNITÉS ===
    @GetMapping("/search")
    public ResponseEntity<List<UnitDTO>> searchUnits(@RequestParam String query) {
        List<Unit> units = unitService.searchUnits(query);
        List<UnitDTO> unitDTOs = units.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unitDTOs);
    }

    // === POST - CRÉER UNE UNITÉ ===
    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody CreateUnitRequest request) {
        try {
            // Validation
            if (request.getUnitCode() == null || request.getUnitCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Le code unité est obligatoire");
            }

            if (request.getUnitDescription() == null || request.getUnitDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La description de l'unité est obligatoire");
            }

            // Vérifier si le code existe déjà
            if (unitService.existsByUnitCode(request.getUnitCode())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une unité avec le code " + request.getUnitCode() + " existe déjà");
            }

            // Créer l'unité
            Unit unit = new Unit();
            unit.setUnitCode(request.getUnitCode());
            unit.setUnitDescription(request.getUnitDescription());
            unit.setSymbol(request.getSymbol());
            unit.setActive(request.getActive() != null ? request.getActive() : true);

            Unit savedUnit = unitService.save(unit);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUnit));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de l'unité: " + e.getMessage());
        }
    }

    // === PUT - METTRE À JOUR UNE UNITÉ ===
    @PutMapping("/{unitCode}")
    public ResponseEntity<?> updateUnit(@PathVariable String unitCode, @RequestBody UpdateUnitRequest request) {
        try {
            return unitService.findByUnitCode(unitCode)
                    .map(existingUnit -> {
                        if (request.getUnitDescription() != null) {
                            existingUnit.setUnitDescription(request.getUnitDescription());
                        }
                        if (request.getSymbol() != null) {
                            existingUnit.setSymbol(request.getSymbol());
                        }
                        if (request.getActive() != null) {
                            existingUnit.setActive(request.getActive());
                        }

                        Unit updatedUnit = unitService.save(existingUnit);
                        return ResponseEntity.ok(convertToDTO(updatedUnit));
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour de l'unité: " + e.getMessage());
        }
    }

    // === DELETE - SUPPRIMER UNE UNITÉ ===
    @DeleteMapping("/{unitCode}")
    public ResponseEntity<?> deleteUnit(@PathVariable String unitCode) {
        try {
            if (unitService.existsByUnitCode(unitCode)) {
                // Vérifier si l'unité est utilisée par des produits
                Unit unit = unitService.findByUnitCode(unitCode).orElse(null);
                if (unit != null && !unit.getProducts().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Impossible de supprimer l'unité car elle est utilisée par " +
                                    unit.getProducts().size() + " produit(s)");
                }

                unitService.deleteByUnitCode(unitCode);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de l'unité: " + e.getMessage());
        }
    }

    // === PATCH - ACTIVER/DÉSACTIVER UNE UNITÉ ===
    @PatchMapping("/{unitCode}/activate")
    public ResponseEntity<?> activateUnit(@PathVariable String unitCode) {
        try {
            Unit activatedUnit = unitService.activateUnit(unitCode);
            return ResponseEntity.ok(convertToDTO(activatedUnit));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{unitCode}/deactivate")
    public ResponseEntity<?> deactivateUnit(@PathVariable String unitCode) {
        try {
            Unit deactivatedUnit = unitService.deactivateUnit(unitCode);
            return ResponseEntity.ok(convertToDTO(deactivatedUnit));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === GET - UNITÉ PAR SYMBOLE ===
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<UnitDTO> getUnitBySymbol(@PathVariable String symbol) {
        return unitService.findBySymbol(symbol)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === MÉTHODE DE CONVERSION ENTITY → DTO ===
    private UnitDTO convertToDTO(Unit unit) {
        UnitDTO dto = new UnitDTO();
        dto.setUnitCode(unit.getUnitCode());
        dto.setUnitDescription(unit.getUnitDescription());
        dto.setSymbol(unit.getSymbol());
        dto.setActive(unit.getActive());
        dto.setProductsCount((long) unit.getProducts().size());
        return dto;
    }
}