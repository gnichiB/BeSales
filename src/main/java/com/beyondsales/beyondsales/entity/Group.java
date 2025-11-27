package com.beyondsales.beyondsales.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_groups")
public class Group {

    @Id
    @Column(name = "group_code", nullable = false, unique = true, length = 50)
    private String groupCode;

    @Column(name = "group_description", length = 500)
    private String groupDescription;

    // === STATUT ===
    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    // === RELATION AVEC PRODUCTS ===
    @OneToMany(mappedBy = "group")
    private List<Product> products = new ArrayList<>();

    // === CONSTRUCTEURS ===
    public Group() {}

    public Group(String groupCode, String groupDescription) {
        this.groupCode = groupCode;
        this.groupDescription = groupDescription;
    }

    // === GETTERS & SETTERS ===
    public String getGroupCode() { return groupCode; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode; }

    public String getGroupDescription() { return groupDescription; }
    public void setGroupDescription(String groupDescription) { this.groupDescription = groupDescription; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // === MÉTHODE POUR AFFICHAGE ===
    @Override
    public String toString() {
        return "Group{" +
                "groupCode='" + groupCode + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", active=" + active +
                '}';
    }
}