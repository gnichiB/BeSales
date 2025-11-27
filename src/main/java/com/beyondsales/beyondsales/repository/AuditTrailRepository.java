package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {

    // Trouver tous les audits d'une table
    List<AuditTrail> findByTableName(String tableName);

    // Trouver tous les audits d'un enregistrement spécifique
    List<AuditTrail> findByTableNameAndRecordId(String tableName, String recordId);

    // Trouver tous les audits d'un utilisateur
    List<AuditTrail> findByChangedBy(String changedBy);
}