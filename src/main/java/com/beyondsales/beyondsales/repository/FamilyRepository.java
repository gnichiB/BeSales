package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, String> {  // String au lieu de Long

    Optional<Family> findByFamilyCode(String familyCode);
    List<Family> findByActiveTrue();
    Boolean existsByFamilyCode(String familyCode);
    List<Family> findByFamilyDescriptionContainingIgnoreCase(String keyword);
}