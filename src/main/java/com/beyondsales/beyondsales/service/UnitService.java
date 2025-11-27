package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.Unit;
import com.beyondsales.beyondsales.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    // === CRUD BASIQUE ===
    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Optional<Unit> findByUnitCode(String unitCode) {
        return unitRepository.findByUnitCode(unitCode);
    }

    public Unit save(Unit unit) {
        return unitRepository.save(unit);
    }

    public void deleteByUnitCode(String unitCode) {
        unitRepository.deleteById(unitCode);
    }

    public boolean existsByUnitCode(String unitCode) {
        return unitRepository.existsById(unitCode);
    }

    // === RECHERCHE AVANCÉE ===
    public List<Unit> searchUnits(String searchTerm) {
        return unitRepository.searchUnits(searchTerm);
    }

    public List<Unit> findByActiveTrue() {
        return unitRepository.findByActiveTrue();
    }

    public List<Unit> findByActiveFalse() {
        return unitRepository.findByActiveFalse();
    }

    // === GESTION DU STATUT ===
    public Unit activateUnit(String unitCode) {
        Unit unit = unitRepository.findByUnitCode(unitCode)
                .orElseThrow(() -> new RuntimeException("Unité non trouvée: " + unitCode));
        unit.setActive(true);
        return unitRepository.save(unit);
    }

    public Unit deactivateUnit(String unitCode) {
        Unit unit = unitRepository.findByUnitCode(unitCode)
                .orElseThrow(() -> new RuntimeException("Unité non trouvée: " + unitCode));
        unit.setActive(false);
        return unitRepository.save(unit);
    }

    // === RECHERCHE PAR SYMBOLE ===
    public Optional<Unit> findBySymbol(String symbol) {
        return unitRepository.findBySymbol(symbol);
    }

    // === VALIDATION ===
    public boolean isUnitCodeAvailable(String unitCode) {
        return !unitRepository.existsById(unitCode);
    }
}