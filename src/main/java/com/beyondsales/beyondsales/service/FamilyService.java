package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.Family;
import com.beyondsales.beyondsales.entity.AuditTrail;
import com.beyondsales.beyondsales.repository.FamilyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final AuditService auditService;

    public FamilyService(FamilyRepository familyRepository, AuditService auditService) {
        this.familyRepository = familyRepository;
        this.auditService = auditService;
    }

    // === CRUD AVEC AUDIT ===

    public List<Family> findAll() {
        return familyRepository.findAll();
    }

    public Optional<Family> findByFamilyCode(String familyCode) {
        return familyRepository.findByFamilyCode(familyCode);
    }

    public Family createFamily(Family family, String username) {
        // Vérifier si le code existe déjà
        if (familyRepository.existsByFamilyCode(family.getFamilyCode())) {
            throw new RuntimeException("Le code famille existe déjà: " + family.getFamilyCode());
        }

        // Sauvegarder la famille
        Family savedFamily = familyRepository.save(family);

        // Audit de la création
        auditService.logAction(
                "families",
                savedFamily.getFamilyCode(),
                AuditTrail.ActionType.CREATE,
                username
        );

        return savedFamily;
    }

    public Family updateFamily(String familyCode, Family familyDetails, String username) {
        return familyRepository.findByFamilyCode(familyCode)
                .map(existingFamily -> {
                    // Mettre à jour
                    existingFamily.setFamilyDescription(familyDetails.getFamilyDescription());
                    existingFamily.setActive(familyDetails.getActive());

                    Family updatedFamily = familyRepository.save(existingFamily);

                    // Audit de la modification
                    auditService.logAction(
                            "families",
                            familyCode,
                            AuditTrail.ActionType.UPDATE,
                            username
                    );

                    return updatedFamily;
                })
                .orElseThrow(() -> new RuntimeException("Famille non trouvée: " + familyCode));
    }

    public void deleteFamily(String familyCode, String username) {
        familyRepository.findByFamilyCode(familyCode)
                .ifPresent(family -> {
                    // Vérifier s'il y a des produits associés
                    if (!family.getProducts().isEmpty()) {
                        throw new RuntimeException("Impossible de supprimer la famille: des produits y sont associés");
                    }

                    // Audit avant suppression
                    auditService.logAction(
                            "product_families",
                            familyCode,
                            AuditTrail.ActionType.DELETE,
                            username
                    );

                    // Supprimer la famille
                    familyRepository.deleteById(familyCode);
                });
    }

    public boolean existsByFamilyCode(String familyCode) {
        return familyRepository.existsByFamilyCode(familyCode);
    }

    public List<Family> findActiveFamilies() {
        return familyRepository.findByActiveTrue();
    }

    public List<Family> findByFamilyDescriptionContainingIgnoreCase(String keyword) {
        return familyRepository.findByFamilyDescriptionContainingIgnoreCase(keyword);
    }
}