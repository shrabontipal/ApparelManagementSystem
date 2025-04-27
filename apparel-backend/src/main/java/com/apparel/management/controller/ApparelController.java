package com.apparel.management.controller;

import com.apparel.management.model.Apparel;
import com.apparel.management.service.ApparelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/apparels")
@CrossOrigin(origins = "*")
public class ApparelController {
    
    private final ApparelService apparelService;
    
    @Autowired
    public ApparelController(ApparelService apparelService) {
        this.apparelService = apparelService;
    }
    
    @GetMapping
    public ResponseEntity<List<Apparel>> getAllApparels() {
        List<Apparel> apparels = apparelService.getAllApparels();
        return new ResponseEntity<>(apparels, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Apparel> getApparelById(@PathVariable Long id) {
        Optional<Apparel> apparel = apparelService.getApparelById(id);
        return apparel.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<Apparel> createApparel(@RequestBody Apparel apparel) {
        Apparel savedApparel = apparelService.saveApparel(apparel);
        return new ResponseEntity<>(savedApparel, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Apparel> updateApparel(@PathVariable Long id, @RequestBody Apparel apparel) {
        Optional<Apparel> existingApparel = apparelService.getApparelById(id);
        
        if (existingApparel.isPresent()) {
            apparel.setId(id);
            Apparel updatedApparel = apparelService.saveApparel(apparel);
            return new ResponseEntity<>(updatedApparel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApparel(@PathVariable Long id) {
        Optional<Apparel> existingApparel = apparelService.getApparelById(id);
        
        if (existingApparel.isPresent()) {
            apparelService.deleteApparel(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
