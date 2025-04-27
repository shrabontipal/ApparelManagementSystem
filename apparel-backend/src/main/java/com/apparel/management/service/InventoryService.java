package com.apparel.management.service;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Inventory;
import com.apparel.management.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    
    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    
    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }
    
    public Optional<Inventory> getInventoryItemById(Long id) {
        return inventoryRepository.findById(id);
    }
    
    public Optional<Inventory> getInventoryItemByApparel(Apparel apparel) {
        return inventoryRepository.findByApparel(apparel);
    }
    
    public Optional<Inventory> getInventoryItemByApparelId(Long apparelId) {
        return inventoryRepository.findByApparelId(apparelId);
    }
    
    public Inventory saveInventoryItem(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    
    public boolean decreaseInventoryQuantity(Long apparelId, int quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByApparelId(apparelId);
        
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            if (inventory.getQuantity() >= quantity) {
                inventory.setQuantity(inventory.getQuantity() - quantity);
                inventoryRepository.save(inventory);
                return true;
            }
        }
        return false;
    }
    
    public void increaseInventoryQuantity(Long apparelId, int quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByApparelId(apparelId);
        
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventoryRepository.save(inventory);
        }
    }
}
