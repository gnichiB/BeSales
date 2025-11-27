package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_types")
public class Type {

    @Id
    @Column(name = "type_code", nullable = false, unique = true, length = 50)
    private String typeCode;

    @Column(name = "type_description", length = 500)
    private String typeDescription;

    // === STATUT ===
    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    // === RELATION AVEC PRODUCTS ===
    @OneToMany(mappedBy = "type")
    private List<Product> products = new ArrayList<>();

    // === CONSTRUCTEURS ===
    public Type() {}

    public Type(String typeCode, String typeDescription) {
        this.typeCode = typeCode;
        this.typeDescription = typeDescription;
    }

    // === GETTERS & SETTERS ===
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }

    public String getTypeDescription() { return typeDescription; }
    public void setTypeDescription(String typeDescription) { this.typeDescription = typeDescription; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // === MÉTHODE POUR AFFICHAGE ===
    @Override
    public String toString() {
        return "Type{" +
                "typeCode='" + typeCode + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", active=" + active +
                '}';
    }
}