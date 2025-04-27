package com.apparel.management.repository;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByApparel(Apparel apparel);
    Optional<Inventory> findByApparelId(Long apparelId);
}
