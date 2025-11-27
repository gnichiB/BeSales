package com.beyondsales.beyondsales.dto;

import com.beyondsales.beyondsales.entity.Category;
import com.beyondsales.beyondsales.entity.Family;
import com.beyondsales.beyondsales.entity.Type;
import com.beyondsales.beyondsales.entity.Group;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDTO {
    private Long productId;
    private String productCode;
    private String reference;
    private String designation;
    private String unit;

    // === RELATIONS NORMALISÉES (objets entiers) ===
    private Category category;
    private Family family;
    private Type type;
    private Group group;

    // === NOMS DES RELATIONS (pour affichage) ===
    private String categoryName;
    private String familyName;
    private String typeName;
    private String groupName;

    // === INFORMATIONS STOCK ET PRIX ===
    private String taxClass;
    private Integer safetyStock;
    private Integer physicalStock;
    private Integer reservedStock;
    private Integer availableStock;
    private Boolean active;
    private BigDecimal defaultPrice;

    // === DATES ===
    private LocalDateTime creationDate;
    private LocalDateTime lastEntryDate;
    private LocalDateTime lastExitDate;

    // === CHAMPS CALCULÉS ===
    private Boolean needsReorder;

    // === INFORMATIONS SUPPLÉMENTAIRES ===
    private String saleLabel;
    private String purchaseLabel;
    private Boolean multiLocation;
    private String defaultWarehouse;
    private String aisle;
    private String shelf;
    private String level;
    private Boolean batchManaged;
    private Boolean perishable;
    private Integer defaultExpiryDays;
    private BigDecimal standardCost;
    private BigDecimal weightedAverageCost;

    // Constructeurs
    public ProductDTO() {}

    public ProductDTO(Long productId, String productCode, String designation) {
        this.productId = productId;
        this.productCode = productCode;
        this.designation = designation;
    }

    // Getters et Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    // === RELATIONS ===
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Family getFamily() { return family; }
    public void setFamily(Family family) { this.family = family; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    // === NOMS DES RELATIONS ===
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    // === STOCK ET PRIX ===
    public String getTaxClass() { return taxClass; }
    public void setTaxClass(String taxClass) { this.taxClass = taxClass; }

    public Integer getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }

    public Integer getPhysicalStock() { return physicalStock; }
    public void setPhysicalStock(Integer physicalStock) { this.physicalStock = physicalStock; }

    public Integer getReservedStock() { return reservedStock; }
    public void setReservedStock(Integer reservedStock) { this.reservedStock = reservedStock; }

    public Integer getAvailableStock() { return availableStock; }
    public void setAvailableStock(Integer availableStock) { this.availableStock = availableStock; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public BigDecimal getDefaultPrice() { return defaultPrice; }
    public void setDefaultPrice(BigDecimal defaultPrice) { this.defaultPrice = defaultPrice; }

    // === DATES ===
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getLastEntryDate() { return lastEntryDate; }
    public void setLastEntryDate(LocalDateTime lastEntryDate) { this.lastEntryDate = lastEntryDate; }

    public LocalDateTime getLastExitDate() { return lastExitDate; }
    public void setLastExitDate(LocalDateTime lastExitDate) { this.lastExitDate = lastExitDate; }

    // === CHAMPS CALCULÉS ===
    public Boolean getNeedsReorder() { return needsReorder; }
    public void setNeedsReorder(Boolean needsReorder) { this.needsReorder = needsReorder; }

    // === INFORMATIONS SUPPLÉMENTAIRES ===
    public String getSaleLabel() { return saleLabel; }
    public void setSaleLabel(String saleLabel) { this.saleLabel = saleLabel; }

    public String getPurchaseLabel() { return purchaseLabel; }
    public void setPurchaseLabel(String purchaseLabel) { this.purchaseLabel = purchaseLabel; }

    public Boolean getMultiLocation() { return multiLocation; }
    public void setMultiLocation(Boolean multiLocation) { this.multiLocation = multiLocation; }

    public String getDefaultWarehouse() { return defaultWarehouse; }
    public void setDefaultWarehouse(String defaultWarehouse) { this.defaultWarehouse = defaultWarehouse; }

    public String getAisle() { return aisle; }
    public void setAisle(String aisle) { this.aisle = aisle; }

    public String getShelf() { return shelf; }
    public void setShelf(String shelf) { this.shelf = shelf; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Boolean getBatchManaged() { return batchManaged; }
    public void setBatchManaged(Boolean batchManaged) { this.batchManaged = batchManaged; }

    public Boolean getPerishable() { return perishable; }
    public void setPerishable(Boolean perishable) { this.perishable = perishable; }

    public Integer getDefaultExpiryDays() { return defaultExpiryDays; }
    public void setDefaultExpiryDays(Integer defaultExpiryDays) { this.defaultExpiryDays = defaultExpiryDays; }

    public BigDecimal getStandardCost() { return standardCost; }
    public void setStandardCost(BigDecimal standardCost) { this.standardCost = standardCost; }

    public BigDecimal getWeightedAverageCost() { return weightedAverageCost; }
    public void setWeightedAverageCost(BigDecimal weightedAverageCost) { this.weightedAverageCost = weightedAverageCost; }

    // === MÉTHODES UTILITAIRES ===
    @Override
    public String toString() {
        return "ProductDTO{" +
                "productCode='" + productCode + '\'' +
                ", designation='" + designation + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", active=" + active +
                '}';
    }
}