package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.AuditTrail;
import com.beyondsales.beyondsales.repository.AuditTrailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuditService {

    private final AuditTrailRepository auditRepository;

    public AuditService(AuditTrailRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Logger une action simple (qui, quoi, quand)
     */
    public void logAction(String tableName, String recordId,
                          AuditTrail.ActionType actionType, String changedBy) {

        AuditTrail audit = new AuditTrail(tableName, recordId, actionType, changedBy);
        auditRepository.save(audit);
    }

    /**
     * Obtenir l'historique d'une table
     */
    public List<AuditTrail> getTableHistory(String tableName) {
        return auditRepository.findByTableName(tableName);
    }

    /**
     * Obtenir l'historique d'un enregistrement spécifique
     */
    public List<AuditTrail> getRecordHistory(String tableName, String recordId) {
        return auditRepository.findByTableNameAndRecordId(tableName, recordId);
    }

    /**
     * Obtenir toutes les actions d'un utilisateur
     */
    public List<AuditTrail> getUserActions(String username) {
        return auditRepository.findByChangedBy(username);
    }
}