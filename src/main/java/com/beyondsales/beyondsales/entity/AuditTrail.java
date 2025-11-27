package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;          // Ex: "products", "categories"

    @Column(name = "record_id", nullable = false, length = 100)
    private String recordId;           // Ex: "P001", "ELEC"

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 10)
    private ActionType actionType;     // CREATE, UPDATE, DELETE

    @Column(name = "changed_by", length = 100)
    private String changedBy;          // Utilisateur qui a fait l'action

    @Column(name = "changed_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime changedAt;   // Quand l'action a été faite

    // === ENUM ===
    public enum ActionType {
        CREATE, UPDATE, DELETE
    }

    // === CONSTRUCTEURS ===
    public AuditTrail() {}

    public AuditTrail(String tableName, String recordId, ActionType actionType, String changedBy) {
        this.tableName = tableName;
        this.recordId = recordId;
        this.actionType = actionType;
        this.changedBy = changedBy;
        this.changedAt = LocalDateTime.now();
    }

    // === GETTERS & SETTERS ===
    public Long getAuditId() { return auditId; }
    public void setAuditId(Long auditId) { this.auditId = auditId; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    @Override
    public String toString() {
        return "AuditTrail{" +
                "table='" + tableName + '\'' +
                ", record='" + recordId + '\'' +
                ", action=" + actionType +
                ", by='" + changedBy + '\'' +
                ", at=" + changedAt +
                '}';
    }
}