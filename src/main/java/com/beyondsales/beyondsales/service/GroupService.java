package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.Group;
import com.beyondsales.beyondsales.entity.AuditTrail;
import com.beyondsales.beyondsales.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final AuditService auditService;

    public GroupService(GroupRepository groupRepository, AuditService auditService) {
        this.groupRepository = groupRepository;
        this.auditService = auditService;
    }

    // === CRUD AVEC AUDIT ===

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> findByGroupCode(String groupCode) {
        return groupRepository.findByGroupCode(groupCode);
    }

    public Group createGroup(Group group, String username) {
        // Vérifier si le code existe déjà
        if (groupRepository.existsByGroupCode(group.getGroupCode())) {
            throw new RuntimeException("Le code groupe existe déjà: " + group.getGroupCode());
        }

        // Sauvegarder le groupe
        Group savedGroup = groupRepository.save(group);

        // Audit de la création
        auditService.logAction(
                "groups",
                savedGroup.getGroupCode(),
                AuditTrail.ActionType.CREATE,
                username
        );

        return savedGroup;
    }

    public Group updateGroup(String groupCode, Group groupDetails, String username) {
        return groupRepository.findByGroupCode(groupCode)
                .map(existingGroup -> {
                    // Mettre à jour
                    existingGroup.setGroupDescription(groupDetails.getGroupDescription());
                    existingGroup.setActive(groupDetails.getActive());

                    Group updatedGroup = groupRepository.save(existingGroup);

                    // Audit de la modification
                    auditService.logAction(
                            "groups",
                            groupCode,
                            AuditTrail.ActionType.UPDATE,
                            username
                    );

                    return updatedGroup;
                })
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé: " + groupCode));
    }

    public void deleteGroup(String groupCode, String username) {
        groupRepository.findByGroupCode(groupCode)
                .ifPresent(group -> {
                    // Vérifier s'il y a des produits associés
                    if (!group.getProducts().isEmpty()) {
                        throw new RuntimeException("Impossible de supprimer le groupe: des produits y sont associés");
                    }

                    // Audit avant suppression
                    auditService.logAction(
                            "product_groups",
                            groupCode,
                            AuditTrail.ActionType.DELETE,
                            username
                    );

                    // Supprimer le groupe
                    groupRepository.deleteById(groupCode);
                });
    }

    public boolean existsByGroupCode(String groupCode) {
        return groupRepository.existsByGroupCode(groupCode);
    }

    public List<Group> findActiveGroups() {
        return groupRepository.findByActiveTrue();
    }

    public List<Group> findByGroupDescriptionContainingIgnoreCase(String keyword) {
        return groupRepository.findByGroupDescriptionContainingIgnoreCase(keyword);
    }

    public List<Group> findByGroupCodeContainingIgnoreCase(String groupCode) {
        return groupRepository.findByGroupCodeContainingIgnoreCase(groupCode);
    }
}