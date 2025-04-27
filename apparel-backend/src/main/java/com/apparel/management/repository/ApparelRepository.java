package com.apparel.management.repository;

import com.apparel.management.model.Apparel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApparelRepository extends JpaRepository<Apparel, Long> {
    Optional<Apparel> findByName(String name);
}
