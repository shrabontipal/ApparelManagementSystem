package com.apparel.management.controller;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Warehouse;
import com.apparel.management.service.ApparelService;
import com.apparel.management.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/warehouse")
@CrossOrigin(origins = "*")
public class WarehouseController {
    
    private final WarehouseService warehouseService;
    private final ApparelService apparelService;
    
    @Autowired
    public WarehouseController(WarehouseService warehouseService, ApparelService apparelService) {
        this.warehouseService = warehouseService;
        this.apparelService = apparelService;
    }
    
    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouseItems() {
        List<Warehouse> warehouseItems = warehouseService.getAllWarehouseItems();
        return new ResponseEntity<>(warehouseItems, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseItemById(@PathVariable Long id) {
        Optional<Warehouse> warehouseItem = warehouseService.getWarehouseItemById(id);
        return warehouseItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/apparel/{apparelId}")
    public ResponseEntity<Warehouse> getWarehouseItemByApparelId(@PathVariable Long apparelId) {
        Optional<Warehouse> warehouseItem = warehouseService.getWarehouseItemByApparelId(apparelId);
        return warehouseItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouseItem(@RequestBody Warehouse warehouse) {
        Optional<Apparel> apparel = apparelService.getApparelById(warehouse.getApparel().getId());
        
        if (apparel.isPresent()) {
            warehouse.setApparel(apparel.get());
            Warehouse savedWarehouse = warehouseService.saveWarehouseItem(warehouse);
            return new ResponseEntity<>(savedWarehouse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouseItem(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        Optional<Warehouse> existingWarehouse = warehouseService.getWarehouseItemById(id);
        
        if (existingWarehouse.isPresent()) {
            Optional<Apparel> apparel = apparelService.getApparelById(warehouse.getApparel().getId());
            
            if (apparel.isPresent()) {
                warehouse.setId(id);
                warehouse.setApparel(apparel.get());
                Warehouse updatedWarehouse = warehouseService.saveWarehouseItem(warehouse);
                return new ResponseEntity<>(updatedWarehouse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/quantity/{quantity}")
    public ResponseEntity<Warehouse> updateWarehouseQuantity(@PathVariable Long id, @PathVariable int quantity) {
        Optional<Warehouse> existingWarehouse = warehouseService.getWarehouseItemById(id);
        
        if (existingWarehouse.isPresent()) {
            Warehouse warehouse = existingWarehouse.get();
            warehouse.setQuantity(quantity);
            Warehouse updatedWarehouse = warehouseService.saveWarehouseItem(warehouse);
            return new ResponseEntity<>(updatedWarehouse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
