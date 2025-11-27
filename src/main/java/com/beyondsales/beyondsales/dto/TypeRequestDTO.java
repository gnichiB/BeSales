package com.beyondsales.beyondsales.dto;

public class TypeRequestDTO {
    private String typeCode;
    private String typeDescription;
    private Boolean active;

    // Constructeurs
    public TypeRequestDTO() {}

    public TypeRequestDTO(String typeCode, String typeDescription, Boolean active) {
        this.typeCode = typeCode;
        this.typeDescription = typeDescription;
        this.active = active;
    }

    // Getters et Setters
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }

    public String getTypeDescription() { return typeDescription; }
    public void setTypeDescription(String typeDescription) { this.typeDescription = typeDescription; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}