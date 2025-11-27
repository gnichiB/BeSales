package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.Category;
import com.beyondsales.beyondsales.entity.AuditTrail;
import com.beyondsales.beyondsales.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuditService auditService;

    public CategoryService(CategoryRepository categoryRepository, AuditService auditService) {
        this.categoryRepository = categoryRepository;
        this.auditService = auditService;
    }

    // === CRUD AVEC AUDIT ===

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findByCategoryCode(String categoryCode) {
        return categoryRepository.findByCategoryCode(categoryCode);
    }

    public Category createCategory(Category category, String username) {
        // Vérifier si le code existe déjà
        if (categoryRepository.existsByCategoryCode(category.getCategoryCode())) {
            throw new RuntimeException("Le code catégorie existe déjà: " + category.getCategoryCode());
        }

        // Sauvegarder la catégorie
        Category savedCategory = categoryRepository.save(category);

        // Audit de la création
        auditService.logAction(
                "categories",
                savedCategory.getCategoryCode(),
                AuditTrail.ActionType.CREATE,
                username
        );

        return savedCategory;
    }

    public Category updateCategory(String categoryCode, Category categoryDetails, String username) {
        return categoryRepository.findByCategoryCode(categoryCode)
                .map(existingCategory -> {
                    // Sauvegarder l'ancienne description pour l'audit
                    String oldDescription = existingCategory.getCategoryDescription();
                    Boolean oldActive = existingCategory.getActive();

                    // Mettre à jour
                    existingCategory.setCategoryDescription(categoryDetails.getCategoryDescription());
                    existingCategory.setActive(categoryDetails.getActive());

                    Category updatedCategory = categoryRepository.save(existingCategory);

                    // Audit de la modification
                    auditService.logAction(
                            "categories",
                            categoryCode,
                            AuditTrail.ActionType.UPDATE,
                            username
                    );

                    return updatedCategory;
                })
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + categoryCode));
    }

    public void deleteCategory(String categoryCode, String username) {
        categoryRepository.findByCategoryCode(categoryCode)
                .ifPresent(category -> {
                    // Vérifier s'il y a des produits associés
                    if (!category.getProducts().isEmpty()) {
                        throw new RuntimeException("Impossible de supprimer la catégorie: des produits y sont associés");
                    }

                    // Audit avant suppression
                    auditService.logAction(
                            "product_categories",
                            categoryCode,
                            AuditTrail.ActionType.DELETE,
                            username
                    );

                    // Supprimer la catégorie
                    categoryRepository.deleteById(categoryCode);
                });
    }

    public boolean existsByCategoryCode(String categoryCode) {
        return categoryRepository.existsByCategoryCode(categoryCode);
    }

    public List<Category> findActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }
}