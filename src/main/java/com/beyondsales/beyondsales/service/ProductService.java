package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.Product;
import com.beyondsales.beyondsales.entity.Category;
import com.beyondsales.beyondsales.repository.ProductRepository;
import com.beyondsales.beyondsales.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // === CRUD BASIQUE ===
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    // === VÉRIFICATIONS D'EXISTENCE ===
    public boolean existsByProductCode(String productCode) {
        return productRepository.existsByProductCode(productCode);
    }

    public boolean existsByReference(String reference) {
        return productRepository.existsByReference(reference);
    }

    // === RECHERCHE AVANCÉE ===
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm);
    }

    public List<Product> findByCategoryCode(String categoryCode) {
        return productRepository.findByCategoryCode(categoryCode);
    }

    public List<Product> findByActiveTrue() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> findByActiveFalse() {
        return productRepository.findByActiveFalse();
    }

    // === GESTION DE STOCK ===
    public List<Product> findProductsWithLowStock() {
        return productRepository.findProductsWithLowStock();
    }

    public List<Product> findProductsNeedingReorder() {
        return productRepository.findProductsNeedingReorder();
    }

    // === MISE À JOUR DU STOCK ===
    public Product updateStock(Long productId, Integer newPhysicalStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + productId));
        product.setPhysicalStock(newPhysicalStock);
        return productRepository.save(product);
    }

    public Product reserveStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + productId));

        if (product.getAvailableStock() >= quantity) {
            product.setReservedStock(product.getReservedStock() + quantity);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Stock insuffisant pour la réservation. Disponible: " + product.getAvailableStock() + ", Demandé: " + quantity);
        }
    }

    public Product releaseReservedStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + productId));

        if (product.getReservedStock() >= quantity) {
            product.setReservedStock(product.getReservedStock() - quantity);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Quantité réservée insuffisante. Réservé: " + product.getReservedStock() + ", Demandé: " + quantity);
        }
    }

    // === RECHERCHE PAR RELATIONS ===
    public List<Product> findByFamilyCode(String familyCode) {
        return productRepository.findByFamilyCode(familyCode);
    }

    public List<Product> findByGroupCode(String groupCode) {
        return productRepository.findByGroupCode(groupCode);
    }

    public List<Product> findByTypeCode(String typeCode) {
        return productRepository.findByTypeCode(typeCode);
    }

    // === RECHERCHE PAR UNITÉ ===
    public List<Product> findByUnitCode(String unitCode) {
        return productRepository.findByUnitCode(unitCode);
    }

    // === GESTION DES CATÉGORIES ===
    public void updateProductCategoryByCode(Product product, String categoryCode) {
        if (categoryCode != null) {
            Category category = categoryRepository.findByCategoryCode(categoryCode)
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + categoryCode));
            product.setCategory(category);
        }
    }

    public Product updateProductCategory(Long productId, String categoryCode) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + productId));

        Category category = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec le code: " + categoryCode));

        product.setCategory(category);
        return productRepository.save(product);
    }

    // === COMPTEURS ===
    public Long countProducts() {
        return productRepository.count();
    }

    public Long countActiveProducts() {
        return productRepository.countByActiveTrue();
    }

    // === CRÉATION AVEC VALIDATION ===
    public Product createProduct(Product product) {
        if (productRepository.existsByProductCode(product.getProductCode())) {
            throw new RuntimeException("Un produit avec le code " + product.getProductCode() + " existe déjà");
        }
        return productRepository.save(product);
    }
}