package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

    Optional<Group> findByGroupCode(String groupCode);
    List<Group> findByActiveTrue();
    Boolean existsByGroupCode(String groupCode);
    List<Group> findByGroupDescriptionContainingIgnoreCase(String keyword);
    List<Group> findByGroupCodeContainingIgnoreCase(String groupCode);
}