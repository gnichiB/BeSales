package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, String> {

    Optional<Unit> findByUnitCode(String unitCode);
    List<Unit> findByActiveTrue();
    List<Unit> findByActiveFalse();

    @Query("SELECT u FROM Unit u WHERE " +
            "LOWER(u.unitCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.unitDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.symbol) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Unit> searchUnits(@Param("search") String search);

    Optional<Unit> findBySymbol(String symbol);
}