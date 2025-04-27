package com.apparel.management.controller;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Inventory;
import com.apparel.management.model.Transaction;
import com.apparel.management.model.Warehouse;
import com.apparel.management.service.ApparelService;
import com.apparel.management.service.InventoryService;
import com.apparel.management.service.TransactionService;
import com.apparel.management.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    private final TransactionService transactionService;
    private final ApparelService apparelService;
    private final InventoryService inventoryService;
    private final WarehouseService warehouseService;
    
    @Autowired
    public TransactionController(TransactionService transactionService, ApparelService apparelService,
                                InventoryService inventoryService, WarehouseService warehouseService) {
        this.transactionService = transactionService;
        this.apparelService = apparelService;
        this.inventoryService = inventoryService;
        this.warehouseService = warehouseService;
    }
    
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/apparel/{apparelId}")
    public ResponseEntity<List<Transaction>> getTransactionsByApparelId(@PathVariable Long apparelId) {
        List<Transaction> transactions = transactionService.getTransactionsByApparelId(apparelId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable Transaction.TransactionType type) {
        List<Transaction> transactions = transactionService.getTransactionsByType(type);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/profit/{apparelId}")
    public ResponseEntity<Map<String, Double>> calculateProfit(@PathVariable Long apparelId) {
        Optional<Apparel> apparel = apparelService.getApparelById(apparelId);
        
        if (apparel.isPresent()) {
            double profit = transactionService.calculateProfit(apparelId);
            return new ResponseEntity<>(Map.of("profit", profit), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/purchase")
    public ResponseEntity<?> processPurchase(@RequestBody Transaction transaction) {
        Optional<Apparel> apparelOptional = apparelService.getApparelById(transaction.getApparel().getId());
        
        if (apparelOptional.isPresent()) {
            Apparel apparel = apparelOptional.get();
            transaction.setApparel(apparel);
            transaction.setType(Transaction.TransactionType.PURCHASE);
            transaction.setAmount(apparel.getSellingPrice() * transaction.getQuantity());
            
            Optional<Inventory> inventoryOptional = inventoryService.getInventoryItemByApparelId(apparel.getId());
            
            if (inventoryOptional.isPresent()) {
                Inventory inventory = inventoryOptional.get();
                
                if (inventory.getQuantity() >= transaction.getQuantity()) {
                    inventoryService.decreaseInventoryQuantity(apparel.getId(), transaction.getQuantity());
                    
                    Transaction savedTransaction = transactionService.saveTransaction(transaction);
                    return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(Map.of("error", "Not enough inventory"), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(Map.of("error", "Inventory not found"), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(Map.of("error", "Apparel not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/refund")
    public ResponseEntity<?> processRefund(@RequestBody Transaction transaction) {
        Optional<Apparel> apparelOptional = apparelService.getApparelById(transaction.getApparel().getId());
        
        if (apparelOptional.isPresent()) {
            Apparel apparel = apparelOptional.get();
            transaction.setApparel(apparel);
            transaction.setType(Transaction.TransactionType.REFUND);
            transaction.setAmount(apparel.getSellingPrice() * transaction.getQuantity());
            
            Optional<Inventory> inventoryOptional = inventoryService.getInventoryItemByApparelId(apparel.getId());
            
            if (inventoryOptional.isPresent()) {
                inventoryService.increaseInventoryQuantity(apparel.getId(), transaction.getQuantity());
                
                Transaction savedTransaction = transactionService.saveTransaction(transaction);
                return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(Map.of("error", "Inventory not found"), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(Map.of("error", "Apparel not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/restock")
    public ResponseEntity<?> restockInventory(@RequestBody Map<String, Object> request) {
        Long apparelId = Long.valueOf(request.get("apparelId").toString());
        int quantity = Integer.parseInt(request.get("quantity").toString());
        
        Optional<Apparel> apparelOptional = apparelService.getApparelById(apparelId);
        
        if (apparelOptional.isPresent()) {
            Apparel apparel = apparelOptional.get();
            
            Optional<Warehouse> warehouseOptional = warehouseService.getWarehouseItemByApparelId(apparelId);
            
            if (warehouseOptional.isPresent()) {
                Warehouse warehouse = warehouseOptional.get();
                
                if (warehouse.getQuantity() >= quantity) {
                    warehouseService.decreaseWarehouseQuantity(apparelId, quantity);
                    
                    inventoryService.increaseInventoryQuantity(apparelId, quantity);
                    
                    Transaction transaction = new Transaction();
                    transaction.setApparel(apparel);
                    transaction.setQuantity(quantity);
                    transaction.setType(Transaction.TransactionType.RESTOCK);
                    transaction.setAmount(apparel.getInventoryCost() * quantity);
                    
                    Transaction savedTransaction = transactionService.saveTransaction(transaction);
                    return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(Map.of("error", "Not enough warehouse stock"), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(Map.of("error", "Warehouse item not found"), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(Map.of("error", "Apparel not found"), HttpStatus.NOT_FOUND);
        }
    }
}
