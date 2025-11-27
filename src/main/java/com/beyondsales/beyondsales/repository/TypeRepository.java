package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {  // String au lieu de Long

    Optional<Type> findByTypeCode(String typeCode);
    List<Type> findByActiveTrue();
    Boolean existsByTypeCode(String typeCode);
    List<Type> findByTypeDescriptionContainingIgnoreCase(String keyword);
    List<Type> findByTypeCodeContainingIgnoreCase(String typeCode);
}