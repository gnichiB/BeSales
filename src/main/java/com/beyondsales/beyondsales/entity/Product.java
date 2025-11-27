package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    // === BASIC IDENTIFICATION ===
    @Column(name = "product_code", nullable = false, unique = true, length = 50)
    private String productCode;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "designation", nullable = false, length = 200)
    private String designation;

    // === UNITÉS DE MESURE ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_code", referencedColumnName = "unit_code")
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_unit_code", referencedColumnName = "unit_code")
    private Unit purchaseUnit;

    @Column(name = "conversion_factor", precision = 10, scale = 4)
    private BigDecimal conversionFactor = BigDecimal.ONE;

    // === CLASSIFICATION NORMALISÉE ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code", referencedColumnName = "category_code")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_code", referencedColumnName = "family_code")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code", referencedColumnName = "type_code")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_code", referencedColumnName = "group_code")
    private Group group;

    // === PLAN AND VERSION ===
    @Column(name = "plan_reference", length = 100)
    private String planReference;

    @Column(name = "version", length = 10)
    private String version;

    @Column(name = "subversion", length = 10)
    private String subversion;

    // === PACKAGING AND WEIGHT ===
    @Column(name = "packaging")
    private Integer packaging;

    @Column(name = "weight", precision = 10, scale = 3)
    private BigDecimal weight;

    // === LEAD TIMES ===
    @Column(name = "purchase_lead_time")
    private Integer purchaseLeadTime;

    @Column(name = "manufacturing_lead_time")
    private Integer manufacturingLeadTime;

    @Column(name = "reception_lead_time")
    private Integer receptionLeadTime;

    // === STOCK MANAGEMENT ===
    @Column(name = "self_service")
    private Boolean selfService = false;

    @Column(name = "moq")
    private Integer moq = 0;

    @Column(name = "multiple_quantity")
    private Integer multipleQuantity = 0;

    @Column(name = "max_alert_threshold")
    private Integer maxAlertThreshold = 0;

    // === SUPPLIER AND CUSTOMER ===
    @Column(name = "default_supplier_id")
    private Long defaultSupplierId;

    @Column(name = "default_customer_id")
    private Long defaultCustomerId;

    // === EXTERNAL REFERENCES ===
    @Column(name = "customer_reference", length = 100)
    private String customerReference;

    @Column(name = "supplier_reference", length = 100)
    private String supplierReference;

    // === TAX ===
    @Column(name = "tax_class", length = 10)
    private String taxClass;

    // === STOCK LEVELS ===
    @Column(name = "safety_stock")
    private Integer safetyStock = 0;

    @Column(name = "physical_stock")
    private Integer physicalStock = 0;

    @Column(name = "reserved_stock")
    private Integer reservedStock = 0;

    // === DATES ===
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "last_entry_date")
    private LocalDateTime lastEntryDate;

    @Column(name = "last_exit_date")
    private LocalDateTime lastExitDate;

    // === LABELS ===
    @Column(name = "sale_label", length = 200)
    private String saleLabel;

    @Column(name = "purchase_label", length = 200)
    private String purchaseLabel;

    // === IMAGE ===
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    // === STATUS AND PRICING ===
    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "default_price", precision = 10, scale = 3)
    private BigDecimal defaultPrice;

    // === MULTI-LOCATION ===
    @Column(name = "multi_location")
    private Boolean multiLocation = false;

    @Column(name = "default_warehouse", length = 100)
    private String defaultWarehouse;

    @Column(name = "aisle", length = 50)
    private String aisle;

    @Column(name = "shelf", length = 50)
    private String shelf;

    @Column(name = "level", length = 50)
    private String level;

    // === BATCH MANAGEMENT ===
    @Column(name = "batch_managed")
    private Boolean batchManaged = false;

    @Column(name = "perishable")
    private Boolean perishable = false;

    @Column(name = "default_expiry_days")
    private Integer defaultExpiryDays;

    // === COSTS ===
    @Column(name = "standard_cost", precision = 10, scale = 5)
    private BigDecimal standardCost;

    @Column(name = "weighted_average_cost", precision = 10, scale = 5)
    private BigDecimal weightedAverageCost;

    // === CONSTRUCTORS ===
    public Product() {
        this.creationDate = LocalDateTime.now();
    }

    public Product(String productCode, String designation, Category category) {
        this();
        this.productCode = productCode;
        this.designation = designation;
        this.category = category;
    }

    public Product(String productCode, String designation, Category category, Unit unit) {
        this(productCode, designation, category);
        this.unit = unit;
    }

    // === GETTERS & SETTERS ===
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    // === UNITÉS ===
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public Unit getPurchaseUnit() { return purchaseUnit; }
    public void setPurchaseUnit(Unit purchaseUnit) { this.purchaseUnit = purchaseUnit; }

    public BigDecimal getConversionFactor() { return conversionFactor; }
    public void setConversionFactor(BigDecimal conversionFactor) { this.conversionFactor = conversionFactor; }

    // === RELATIONS NORMALISÉES ===
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Family getFamily() { return family; }
    public void setFamily(Family family) { this.family = family; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    // === AUTRES GETTERS/SETTERS ===
    public String getPlanReference() { return planReference; }
    public void setPlanReference(String planReference) { this.planReference = planReference; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getSubversion() { return subversion; }
    public void setSubversion(String subversion) { this.subversion = subversion; }

    public Integer getPackaging() { return packaging; }
    public void setPackaging(Integer packaging) { this.packaging = packaging; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public Integer getPurchaseLeadTime() { return purchaseLeadTime; }
    public void setPurchaseLeadTime(Integer purchaseLeadTime) { this.purchaseLeadTime = purchaseLeadTime; }

    public Integer getManufacturingLeadTime() { return manufacturingLeadTime; }
    public void setManufacturingLeadTime(Integer manufacturingLeadTime) { this.manufacturingLeadTime = manufacturingLeadTime; }

    public Integer getReceptionLeadTime() { return receptionLeadTime; }
    public void setReceptionLeadTime(Integer receptionLeadTime) { this.receptionLeadTime = receptionLeadTime; }

    public Boolean getSelfService() { return selfService; }
    public void setSelfService(Boolean selfService) { this.selfService = selfService; }

    public Integer getMoq() { return moq; }
    public void setMoq(Integer moq) { this.moq = moq; }

    public Integer getMultipleQuantity() { return multipleQuantity; }
    public void setMultipleQuantity(Integer multipleQuantity) { this.multipleQuantity = multipleQuantity; }

    public Integer getMaxAlertThreshold() { return maxAlertThreshold; }
    public void setMaxAlertThreshold(Integer maxAlertThreshold) { this.maxAlertThreshold = maxAlertThreshold; }

    public Long getDefaultSupplierId() { return defaultSupplierId; }
    public void setDefaultSupplierId(Long defaultSupplierId) { this.defaultSupplierId = defaultSupplierId; }

    public Long getDefaultCustomerId() { return defaultCustomerId; }
    public void setDefaultCustomerId(Long defaultCustomerId) { this.defaultCustomerId = defaultCustomerId; }

    public String getCustomerReference() { return customerReference; }
    public void setCustomerReference(String customerReference) { this.customerReference = customerReference; }

    public String getSupplierReference() { return supplierReference; }
    public void setSupplierReference(String supplierReference) { this.supplierReference = supplierReference; }

    public String getTaxClass() { return taxClass; }
    public void setTaxClass(String taxClass) { this.taxClass = taxClass; }

    public Integer getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }

    public Integer getPhysicalStock() { return physicalStock; }
    public void setPhysicalStock(Integer physicalStock) { this.physicalStock = physicalStock; }

    public Integer getReservedStock() { return reservedStock; }
    public void setReservedStock(Integer reservedStock) { this.reservedStock = reservedStock; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getLastEntryDate() { return lastEntryDate; }
    public void setLastEntryDate(LocalDateTime lastEntryDate) { this.lastEntryDate = lastEntryDate; }

    public LocalDateTime getLastExitDate() { return lastExitDate; }
    public void setLastExitDate(LocalDateTime lastExitDate) { this.lastExitDate = lastExitDate; }

    public String getSaleLabel() { return saleLabel; }
    public void setSaleLabel(String saleLabel) { this.saleLabel = saleLabel; }

    public String getPurchaseLabel() { return purchaseLabel; }
    public void setPurchaseLabel(String purchaseLabel) { this.purchaseLabel = purchaseLabel; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public BigDecimal getDefaultPrice() { return defaultPrice; }
    public void setDefaultPrice(BigDecimal defaultPrice) { this.defaultPrice = defaultPrice; }

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

    // === CALCULATED FIELDS ===
    @Transient
    public Integer getAvailableStock() {
        return physicalStock - reservedStock;
    }

    @Transient
    public Boolean needsReorder() {
        return getAvailableStock() <= safetyStock;
    }

    @Transient
    public String getUnitSymbol() {
        return unit != null ? unit.getSymbol() : "";
    }

    @Transient
    public String getPurchaseUnitSymbol() {
        return purchaseUnit != null ? purchaseUnit.getSymbol() : "";
    }

    @Transient
    public String getUnitDescription() {
        return unit != null ? unit.getUnitDescription() : "";
    }

    @Transient
    public String getPurchaseUnitDescription() {
        return purchaseUnit != null ? purchaseUnit.getUnitDescription() : "";
    }

    @Transient
    public BigDecimal convertToPurchaseUnit(BigDecimal quantity) {
        if (purchaseUnit != null && unit != null && conversionFactor != null) {
            return quantity.multiply(conversionFactor);
        }
        return quantity;
    }

    @Transient
    public BigDecimal getStockValue() {
        if (weightedAverageCost != null && physicalStock != null) {
            return weightedAverageCost.multiply(BigDecimal.valueOf(physicalStock));
        }
        return BigDecimal.ZERO;
    }

    // === CHAMPS CALCULÉS POUR LES RELATIONS ===
    @Transient
    public String getCategoryName() {
        return category != null ? category.getCategoryDescription() : "";
    }

    @Transient
    public String getFamilyName() {
        return family != null ? family.getFamilyDescription() : "";
    }

    @Transient
    public String getTypeName() {
        return type != null ? type.getTypeDescription() : "";
    }

    @Transient
    public String getGroupName() {
        return group != null ? group.getGroupDescription() : "";
    }

    @Override
    public String toString() {
        return "Product{" +
                "productCode='" + productCode + '\'' +
                ", designation='" + designation + '\'' +
                ", category=" + (category != null ? category.getCategoryCode() : "null") +
                ", unit=" + (unit != null ? unit.getUnitCode() : "null") +
                '}';
    }
}