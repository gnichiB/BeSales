package com.beyondsales.beyondsales.controller;

import com.beyondsales.beyondsales.entity.Category;
import com.beyondsales.beyondsales.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // === GET - TOUTES LES CATÉGORIES ===
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    // === GET - CATÉGORIES ACTIVES ===
    @GetMapping("/active")
    public ResponseEntity<List<Category>> getActiveCategories() {
        List<Category> categories = categoryService.findActiveCategories();
        return ResponseEntity.ok(categories);
    }

    // === GET - CATÉGORIE PAR CODE ===
    @GetMapping("/{categoryCode}")
    public ResponseEntity<Category> getCategoryByCode(@PathVariable String categoryCode) {
        Optional<Category> category = categoryService.findByCategoryCode(categoryCode);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST - CRÉER UNE CATÉGORIE ===
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category,
                                            @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Category savedCategory = categoryService.createCategory(category, currentUser);
            return ResponseEntity.ok(savedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === PUT - METTRE À JOUR UNE CATÉGORIE ===
    @PutMapping("/{categoryCode}")
    public ResponseEntity<?> updateCategory(@PathVariable String categoryCode,
                                            @RequestBody Category categoryDetails,
                                            @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Category updatedCategory = categoryService.updateCategory(categoryCode, categoryDetails, currentUser);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === DELETE - SUPPRIMER UNE CATÉGORIE ===
    @DeleteMapping("/{categoryCode}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryCode,
                                            @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            categoryService.deleteCategory(categoryCode, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}