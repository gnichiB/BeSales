package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
public class Unit {

    @Id
    @Column(name = "unit_code", nullable = false, unique = true, length = 10)
    private String unitCode;

    @Column(name = "unit_description", length = 100)
    private String unitDescription;

    @Column(name = "symbol", length = 5)
    private String symbol; // m, kg, L, pce, h, etc.

    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    // === RELATION AVEC PRODUCTS ===
    @OneToMany(mappedBy = "unit")
    private List<Product> products = new ArrayList<>();

    // === CONSTRUCTEURS ===
    public Unit() {}

    public Unit(String unitCode, String unitDescription) {
        this.unitCode = unitCode;
        this.unitDescription = unitDescription;
    }

    public Unit(String unitCode, String unitDescription, String symbol) {
        this.unitCode = unitCode;
        this.unitDescription = unitDescription;
        this.symbol = symbol;
    }

    // === GETTERS & SETTERS ===
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }

    public String getUnitDescription() { return unitDescription; }
    public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // === MÉTHODE POUR AFFICHAGE ===
    @Override
    public String toString() {
        return "Unit{" +
                "unitCode='" + unitCode + '\'' +
                ", unitDescription='" + unitDescription + '\'' +
                ", symbol='" + symbol + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return unitCode != null && unitCode.equals(unit.unitCode);
    }

    @Override
    public int hashCode() {
        return unitCode != null ? unitCode.hashCode() : 0;
    }
}