package com.beyondsales.beyondsales.dto;

public class CreateUnitRequest {
    private String unitCode;
    private String unitDescription;
    private String symbol;
    private Boolean active;

    // Constructeurs
    public CreateUnitRequest() {}

    public CreateUnitRequest(String unitCode, String unitDescription, String symbol) {
        this.unitCode = unitCode;
        this.unitDescription = unitDescription;
        this.symbol = symbol;
        this.active = true;
    }

    // Getters et Setters
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }

    public String getUnitDescription() { return unitDescription; }
    public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}