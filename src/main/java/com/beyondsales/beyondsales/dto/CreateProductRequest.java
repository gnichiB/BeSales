package com.beyondsales.beyondsales.dto;

import java.math.BigDecimal;

public class CreateProductRequest {
    private String productCode;
    private String reference;
    private String designation;
    private String unitCode;
    private String categoryCode;
    private String taxClass;
    private Integer safetyStock;
    private BigDecimal defaultPrice;
    private Boolean active = true;

    // Constructeurs
    public CreateProductRequest() {}

    public CreateProductRequest(String productCode, String designation) {
        this.productCode = productCode;
        this.designation = designation;
    }

    // Getters et Setters
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getTaxClass() { return taxClass; }
    public void setTaxClass(String taxClass) { this.taxClass = taxClass; }

    public Integer getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }

    public BigDecimal getDefaultPrice() { return defaultPrice; }
    public void setDefaultPrice(BigDecimal defaultPrice) { this.defaultPrice = defaultPrice; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}