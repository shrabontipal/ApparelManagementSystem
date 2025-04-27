package com.apparel.management.config;

import com.apparel.management.model.*;
import com.apparel.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ApparelRepository apparelRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Override
    public void run(String... args) {
        String adminUsername = System.getenv("ADMIN_USERNAME") != null ? System.getenv("ADMIN_USERNAME") : "admin";
        String adminPassword = System.getenv("ADMIN_PASSWORD") != null ? System.getenv("ADMIN_PASSWORD") : "password";
        
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.getRoles().add("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin user created with username: " + adminUsername);
        }
        
        if (apparelRepository.count() == 0) {
            Apparel tshirt = new Apparel();
            tshirt.setName("T-Shirt");
            tshirt.setWarehouseCost(6.0);
            tshirt.setInventoryCost(10.0);
            tshirt.setSellingPrice(20.0);
            apparelRepository.save(tshirt);
            
            Apparel hoodie = new Apparel();
            hoodie.setName("Hoodie");
            hoodie.setWarehouseCost(8.0);
            hoodie.setInventoryCost(12.0);
            hoodie.setSellingPrice(40.0);
            apparelRepository.save(hoodie);
            
            Apparel sweater = new Apparel();
            sweater.setName("Sweater");
            sweater.setWarehouseCost(9.0);
            sweater.setInventoryCost(14.0);
            sweater.setSellingPrice(50.0);
            apparelRepository.save(sweater);
            
            System.out.println("Apparel items created");
            
            Warehouse warehouseTshirt = new Warehouse();
            warehouseTshirt.setApparel(tshirt);
            warehouseTshirt.setQuantity(100);
            warehouseRepository.save(warehouseTshirt);
            
            Warehouse warehouseHoodie = new Warehouse();
            warehouseHoodie.setApparel(hoodie);
            warehouseHoodie.setQuantity(50);
            warehouseRepository.save(warehouseHoodie);
            
            Warehouse warehouseSweater = new Warehouse();
            warehouseSweater.setApparel(sweater);
            warehouseSweater.setQuantity(30);
            warehouseRepository.save(warehouseSweater);
            
            System.out.println("Warehouse items created");
            
            Inventory inventoryTshirt = new Inventory();
            inventoryTshirt.setApparel(tshirt);
            inventoryTshirt.setQuantity(20);
            inventoryRepository.save(inventoryTshirt);
            
            Inventory inventoryHoodie = new Inventory();
            inventoryHoodie.setApparel(hoodie);
            inventoryHoodie.setQuantity(10);
            inventoryRepository.save(inventoryHoodie);
            
            Inventory inventorySweater = new Inventory();
            inventorySweater.setApparel(sweater);
            inventorySweater.setQuantity(5);
            inventoryRepository.save(inventorySweater);
            
            System.out.println("Inventory items created");
            
            Transaction purchaseTransaction = new Transaction();
            purchaseTransaction.setApparel(tshirt);
            purchaseTransaction.setQuantity(2);
            purchaseTransaction.setType(Transaction.TransactionType.PURCHASE);
            purchaseTransaction.setTimestamp(LocalDateTime.now());
            purchaseTransaction.setAmount(40.0);
            transactionRepository.save(purchaseTransaction);
            
            Transaction restockTransaction = new Transaction();
            restockTransaction.setApparel(hoodie);
            restockTransaction.setQuantity(5);
            restockTransaction.setType(Transaction.TransactionType.RESTOCK);
            restockTransaction.setTimestamp(LocalDateTime.now());
            restockTransaction.setAmount(60.0);
            transactionRepository.save(restockTransaction);
            
            System.out.println("Sample transactions created");
        }
    }
}
