package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.Product;
import com.beyondsales.beyondsales.entity.Category;
import com.beyondsales.beyondsales.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // === RECHERCHE PAR CODE ET RÉFÉRENCE ===
    Optional<Product> findByProductCode(String productCode);
    Optional<Product> findByReference(String reference);

    // === VÉRIFICATION D'EXISTENCE ===
    Boolean existsByProductCode(String productCode);
    Boolean existsByReference(String reference);

    // === RECHERCHE PAR DÉSIGNATION ===
    List<Product> findByDesignationContainingIgnoreCase(String designation);
    List<Product> findByDesignationStartingWithIgnoreCase(String designation);

    // === RECHERCHE PAR CATÉGORIE ===
    List<Product> findByCategory(Category category);
    @Query("SELECT p FROM Product p WHERE p.category.categoryCode = :categoryCode")
    List<Product> findByCategoryCode(@Param("categoryCode") String categoryCode);

    // === RECHERCHE PAR STATUT ===
    List<Product> findByActiveTrue();
    List<Product> findByActiveFalse();

    // === GESTION DE STOCK ===
    List<Product> findByPhysicalStockLessThan(Integer stock);
    List<Product> findByPhysicalStockGreaterThan(Integer stock);

    // Produits avec stock faible
    @Query("SELECT p FROM Product p WHERE p.physicalStock - p.reservedStock <= p.safetyStock")
    List<Product> findProductsWithLowStock();

    // Produits actifs avec stock positif
    List<Product> findByActiveTrueAndPhysicalStockGreaterThan(Integer stock);

    // === RECHERCHE AVANCÉE ===
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.designation) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.reference) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchProducts(@Param("search") String search);

    // === RECHERCHE PAR RELATIONS ===
    @Query("SELECT p FROM Product p WHERE p.family.familyCode = :familyCode")
    List<Product> findByFamilyCode(@Param("familyCode") String familyCode);

    @Query("SELECT p FROM Product p WHERE p.group.groupCode = :groupCode")
    List<Product> findByGroupCode(@Param("groupCode") String groupCode);

    @Query("SELECT p FROM Product p WHERE p.type.typeCode = :typeCode")
    List<Product> findByTypeCode(@Param("typeCode") String typeCode);

    // === PRODUITS À RÉAPPROVISIONNER ===
    @Query("SELECT p FROM Product p WHERE (p.physicalStock - p.reservedStock) <= p.safetyStock AND p.active = true")
    List<Product> findProductsNeedingReorder();

    // === RECHERCHE PAR PRIX ===
    @Query("SELECT p FROM Product p WHERE p.defaultPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByDefaultPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    // === RECHERCHE PAR UNITÉ ===
    @Query("SELECT p FROM Product p WHERE p.unit.unitCode = :unitCode")
    List<Product> findByUnitCode(@Param("unitCode") String unitCode);

    List<Product> findByUnit(Unit unit);

    // === COMPTEURS ===
    Long countByActiveTrue();
    Long countByCategory(Category category);
}