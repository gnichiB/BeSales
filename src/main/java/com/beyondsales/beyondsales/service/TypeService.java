package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.Type;
import com.beyondsales.beyondsales.entity.AuditTrail;
import com.beyondsales.beyondsales.repository.TypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeService {

    private final TypeRepository typeRepository;
    private final AuditService auditService;

    public TypeService(TypeRepository typeRepository, AuditService auditService) {
        this.typeRepository = typeRepository;
        this.auditService = auditService;
    }

    // === CRUD AVEC AUDIT ===

    public List<Type> findAll() {
        return typeRepository.findAll();
    }

    public Optional<Type> findByTypeCode(String typeCode) {
        return typeRepository.findByTypeCode(typeCode);
    }

    public Type createType(Type type, String username) {
        // Vérifier si le code existe déjà
        if (typeRepository.existsByTypeCode(type.getTypeCode())) {
            throw new RuntimeException("Le code type existe déjà: " + type.getTypeCode());
        }

        // Sauvegarder le type
        Type savedType = typeRepository.save(type);

        // Audit de la création
        auditService.logAction(
                "product_types",
                savedType.getTypeCode(),
                AuditTrail.ActionType.CREATE,
                username
        );

        return savedType;
    }

    public Type updateType(String typeCode, Type typeDetails, String username) {
        return typeRepository.findByTypeCode(typeCode)
                .map(existingType -> {
                    // Mettre à jour
                    existingType.setTypeDescription(typeDetails.getTypeDescription());
                    existingType.setActive(typeDetails.getActive());

                    Type updatedType = typeRepository.save(existingType);

                    // Audit de la modification
                    auditService.logAction(
                            "types",
                            typeCode,
                            AuditTrail.ActionType.UPDATE,
                            username
                    );

                    return updatedType;
                })
                .orElseThrow(() -> new RuntimeException("Type non trouvé: " + typeCode));
    }

    public void deleteType(String typeCode, String username) {
        typeRepository.findByTypeCode(typeCode)
                .ifPresent(type -> {
                    // Vérifier s'il y a des produits associés
                    if (!type.getProducts().isEmpty()) {
                        throw new RuntimeException("Impossible de supprimer le type: des produits y sont associés");
                    }

                    // Audit avant suppression
                    auditService.logAction(
                            "types",
                            typeCode,
                            AuditTrail.ActionType.DELETE,
                            username
                    );

                    // Supprimer le type
                    typeRepository.deleteById(typeCode);
                });
    }

    public boolean existsByTypeCode(String typeCode) {
        return typeRepository.existsByTypeCode(typeCode);
    }

    public List<Type> findActiveTypes() {
        return typeRepository.findByActiveTrue();
    }

    public List<Type> findByTypeDescriptionContainingIgnoreCase(String keyword) {
        return typeRepository.findByTypeDescriptionContainingIgnoreCase(keyword);
    }

    public List<Type> findByTypeCodeContainingIgnoreCase(String typeCode) {
        return typeRepository.findByTypeCodeContainingIgnoreCase(typeCode);
    }
}