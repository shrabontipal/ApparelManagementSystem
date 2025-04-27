package com.apparel.management.repository;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByApparel(Apparel apparel);
    Optional<Warehouse> findByApparelId(Long apparelId);
}
