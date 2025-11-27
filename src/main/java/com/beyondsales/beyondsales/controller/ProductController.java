package com.beyondsales.beyondsales.controller;

import com.beyondsales.beyondsales.entity.Product;
import com.beyondsales.beyondsales.entity.Unit;
import com.beyondsales.beyondsales.service.ProductService;
import com.beyondsales.beyondsales.service.UnitService;
import com.beyondsales.beyondsales.service.CategoryService;
import com.beyondsales.beyondsales.dto.CreateProductRequest;
import com.beyondsales.beyondsales.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final UnitService unitService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, UnitService unitService, CategoryService categoryService) {
        this.productService = productService;
        this.unitService = unitService;
        this.categoryService = categoryService;
    }

    // === GET - LISTER TOUS LES PRODUITS ===
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS AVEC PAGINATION ===
    @GetMapping("/page")
    public ResponseEntity<Page<ProductDTO>> getProductsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productService.findAll(pageable);
        Page<ProductDTO> productDTOs = productsPage.map(this::convertToDTO);
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUIT PAR ID ===
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET - PRODUIT PAR CODE ===
    @GetMapping("/code/{productCode}")
    public ResponseEntity<ProductDTO> getProductByProductCode(@PathVariable String productCode) {
        return productService.findByProductCode(productCode)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST - CRÉER UN PRODUIT ===
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        try {
            // Validation basique
            if (request.getProductCode() == null || request.getProductCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Le code produit est obligatoire");
            }

            if (request.getDesignation() == null || request.getDesignation().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La désignation est obligatoire");
            }

            if (productService.existsByProductCode(request.getProductCode())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un produit avec le code " + request.getProductCode() + " existe déjà");
            }

            Product product = new Product();
            product.setProductCode(request.getProductCode());
            product.setReference(request.getReference());
            product.setDesignation(request.getDesignation());

            // Gestion de l'unité
            if (request.getUnitCode() != null) {
                Unit unit = unitService.findByUnitCode(request.getUnitCode())
                        .orElseThrow(() -> new RuntimeException("Unité non trouvée: " + request.getUnitCode()));
                product.setUnit(unit);
            }

            product.setTaxClass(request.getTaxClass());
            product.setSafetyStock(request.getSafetyStock() != null ? request.getSafetyStock() : 0);
            product.setDefaultPrice(request.getDefaultPrice());
            product.setActive(request.getActive() != null ? request.getActive() : true);

            // Gestion de la catégorie
            if (request.getCategoryCode() != null) {
                productService.updateProductCategoryByCode(product, request.getCategoryCode());
            }

            Product savedProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedProduct));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création du produit: " + e.getMessage());
        }
    }

    // === GET - RECHERCHE DE PRODUITS ===
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        List<Product> products = productService.searchProducts(query);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS PAR CATÉGORIE ===
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String categoryCode) {
        List<Product> products = productService.findByCategoryCode(categoryCode);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS PAR UNITÉ ===
    @GetMapping("/unit/{unitCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByUnit(@PathVariable String unitCode) {
        List<Product> products = productService.findByUnitCode(unitCode);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS ACTIFS ===
    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getActiveProducts() {
        List<Product> products = productService.findByActiveTrue();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS EN RUPTURE DE STOCK ===
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getProductsWithLowStock() {
        List<Product> products = productService.findProductsWithLowStock();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS PAR FAMILLE ===
    @GetMapping("/family/{familyCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByFamily(@PathVariable String familyCode) {
        List<Product> products = productService.findByFamilyCode(familyCode);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS PAR GROUPE ===
    @GetMapping("/group/{groupCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByGroup(@PathVariable String groupCode) {
        List<Product> products = productService.findByGroupCode(groupCode);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS PAR TYPE ===
    @GetMapping("/type/{typeCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByType(@PathVariable String typeCode) {
        List<Product> products = productService.findByTypeCode(typeCode);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GET - PRODUITS À RÉAPPROVISIONNER ===
    @GetMapping("/needs-reorder")
    public ResponseEntity<List<ProductDTO>> getProductsNeedingReorder() {
        List<Product> products = productService.findProductsNeedingReorder();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // === GESTION DE STOCK ===
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam Integer physicalStock) {
        try {
            Product updatedProduct = productService.updateStock(id, physicalStock);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<?> reserveStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            Product updatedProduct = productService.reserveStock(id, quantity);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            Product updatedProduct = productService.releaseReservedStock(id, quantity);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === MÉTHODE DE CONVERSION ENTITY → DTO ===
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductCode(product.getProductCode());
        dto.setReference(product.getReference());
        dto.setDesignation(product.getDesignation());

        // Unités
        if (product.getUnit() != null) {
            dto.setUnit(product.getUnit().getUnitCode());
        }

        // Relations normalisées
        dto.setCategory(product.getCategory());
        dto.setFamily(product.getFamily());
        dto.setGroup(product.getGroup());
        dto.setType(product.getType());

        // Noms des relations
        dto.setCategoryName(product.getCategoryName());
        dto.setFamilyName(product.getFamilyName());
        dto.setGroupName(product.getGroupName());
        dto.setTypeName(product.getTypeName());

        // Stock et prix
        dto.setTaxClass(product.getTaxClass());
        dto.setSafetyStock(product.getSafetyStock());
        dto.setPhysicalStock(product.getPhysicalStock());
        dto.setReservedStock(product.getReservedStock());
        dto.setAvailableStock(product.getAvailableStock());
        dto.setActive(product.getActive());
        dto.setDefaultPrice(product.getDefaultPrice());

        // Dates
        dto.setCreationDate(product.getCreationDate());

        // Champs calculés
        dto.setNeedsReorder(product.needsReorder());

        return dto;
    }
}