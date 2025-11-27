package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_families")
public class Family {

    @Id
    @Column(name = "family_code", nullable = false, unique = true, length = 50)
    private String familyCode;

    @Column(name = "family_description", length = 500)
    private String familyDescription;

    // === STATUT ===
    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    // === RELATION AVEC PRODUCTS ===
    @OneToMany(mappedBy = "family")
    private List<Product> products = new ArrayList<>();

    // === CONSTRUCTEURS ===
    public Family() {}

    public Family(String familyCode, String familyDescription) {
        this.familyCode = familyCode;
        this.familyDescription = familyDescription;
    }

    // === GETTERS & SETTERS ===
    public String getFamilyCode() { return familyCode; }
    public void setFamilyCode(String familyCode) { this.familyCode = familyCode; }

    public String getFamilyDescription() { return familyDescription; }
    public void setFamilyDescription(String familyDescription) { this.familyDescription = familyDescription; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // === MÉTHODE POUR AFFICHAGE ===
    @Override
    public String toString() {
        return "Family{" +
                "familyCode='" + familyCode + '\'' +
                ", familyDescription='" + familyDescription + '\'' +
                ", active=" + active +
                '}';
    }
}