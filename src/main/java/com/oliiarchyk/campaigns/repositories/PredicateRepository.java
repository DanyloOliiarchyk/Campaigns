package com.oliiarchyk.campaigns.repositories;

import com.oliiarchyk.campaigns.models.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredicateRepository extends JpaRepository<Predicate, Long> {

    @Query(value = "SELECT * FROM predicates where camp_id = :id", nativeQuery = true)
    List<Predicate> findAllByCampId(Long id);
}