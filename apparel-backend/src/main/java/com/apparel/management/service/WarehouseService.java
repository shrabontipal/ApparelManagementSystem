package com.apparel.management.service;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Warehouse;
import com.apparel.management.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    
    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }
    
    public List<Warehouse> getAllWarehouseItems() {
        return warehouseRepository.findAll();
    }
    
    public Optional<Warehouse> getWarehouseItemById(Long id) {
        return warehouseRepository.findById(id);
    }
    
    public Optional<Warehouse> getWarehouseItemByApparel(Apparel apparel) {
        return warehouseRepository.findByApparel(apparel);
    }
    
    public Optional<Warehouse> getWarehouseItemByApparelId(Long apparelId) {
        return warehouseRepository.findByApparelId(apparelId);
    }
    
    public Warehouse saveWarehouseItem(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }
    
    public boolean decreaseWarehouseQuantity(Long apparelId, int quantity) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findByApparelId(apparelId);
        
        if (warehouseOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            if (warehouse.getQuantity() >= quantity) {
                warehouse.setQuantity(warehouse.getQuantity() - quantity);
                warehouseRepository.save(warehouse);
                return true;
            }
        }
        return false;
    }
    
    public void increaseWarehouseQuantity(Long apparelId, int quantity) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findByApparelId(apparelId);
        
        if (warehouseOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            warehouse.setQuantity(warehouse.getQuantity() + quantity);
            warehouseRepository.save(warehouse);
        }
    }
}
