package com.apparel.management.controller;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Inventory;
import com.apparel.management.service.ApparelService;
import com.apparel.management.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    private final InventoryService inventoryService;
    private final ApparelService apparelService;
    
    @Autowired
    public InventoryController(InventoryService inventoryService, ApparelService apparelService) {
        this.inventoryService = inventoryService;
        this.apparelService = apparelService;
    }
    
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventoryItems() {
        List<Inventory> inventoryItems = inventoryService.getAllInventoryItems();
        return new ResponseEntity<>(inventoryItems, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryItemById(@PathVariable Long id) {
        Optional<Inventory> inventoryItem = inventoryService.getInventoryItemById(id);
        return inventoryItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/apparel/{apparelId}")
    public ResponseEntity<Inventory> getInventoryItemByApparelId(@PathVariable Long apparelId) {
        Optional<Inventory> inventoryItem = inventoryService.getInventoryItemByApparelId(apparelId);
        return inventoryItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<Inventory> createInventoryItem(@RequestBody Inventory inventory) {
        Optional<Apparel> apparel = apparelService.getApparelById(inventory.getApparel().getId());
        
        if (apparel.isPresent()) {
            inventory.setApparel(apparel.get());
            Inventory savedInventory = inventoryService.saveInventoryItem(inventory);
            return new ResponseEntity<>(savedInventory, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventoryItem(@PathVariable Long id, @RequestBody Inventory inventory) {
        Optional<Inventory> existingInventory = inventoryService.getInventoryItemById(id);
        
        if (existingInventory.isPresent()) {
            Optional<Apparel> apparel = apparelService.getApparelById(inventory.getApparel().getId());
            
            if (apparel.isPresent()) {
                inventory.setId(id);
                inventory.setApparel(apparel.get());
                Inventory updatedInventory = inventoryService.saveInventoryItem(inventory);
                return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/quantity/{quantity}")
    public ResponseEntity<Inventory> updateInventoryQuantity(@PathVariable Long id, @PathVariable int quantity) {
        Optional<Inventory> existingInventory = inventoryService.getInventoryItemById(id);
        
        if (existingInventory.isPresent()) {
            Inventory inventory = existingInventory.get();
            inventory.setQuantity(quantity);
            Inventory updatedInventory = inventoryService.saveInventoryItem(inventory);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
