package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByCategoryCode(String categoryCode);
    List<Category> findByActiveTrue();
    Boolean existsByCategoryCode(String categoryCode);
}