package com.beyondsales.beyondsales.dto;

public class UpdateUnitRequest {
    private String unitDescription;
    private String symbol;
    private Boolean active;

    // Getters et Setters
    public String getUnitDescription() { return unitDescription; }
    public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}