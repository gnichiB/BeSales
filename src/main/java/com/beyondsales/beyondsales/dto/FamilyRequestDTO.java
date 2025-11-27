package com.beyondsales.beyondsales.dto;

public class FamilyRequestDTO {
    private String familyCode;
    private String familyDescription;
    private Boolean active;

    // Constructeurs, Getters et Setters
    public FamilyRequestDTO() {}

    public String getFamilyCode() { return familyCode; }
    public void setFamilyCode(String familyCode) { this.familyCode = familyCode; }

    public String getFamilyDescription() { return familyDescription; }
    public void setFamilyDescription(String familyDescription) { this.familyDescription = familyDescription; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}