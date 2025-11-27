package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_categories")
public class Category {

    @Id
    @Column(name = "category_code", nullable = false, unique = true, length = 20)
    private String categoryCode;

    @Column(name = "category_description", length = 100)
    private String categoryDescription;

    // === STATUT ===
    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    // === RELATION AVEC PRODUCTS ===
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    // === CONSTRUCTEURS ===
    public Category() {}

    public Category(String categoryCode, String categoryDescription) {
        this.categoryCode = categoryCode;
        this.categoryDescription = categoryDescription;
    }

    // === GETTERS & SETTERS ===
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getCategoryDescription() { return categoryDescription; }
    public void setCategoryDescription(String categoryDescription) { this.categoryDescription = categoryDescription; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // === MÉTHODE POUR AFFICHAGE ===
    @Override
    public String toString() {
        return "Category{" +
                "categoryCode='" + categoryCode + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", active=" + active +
                '}';
    }
}