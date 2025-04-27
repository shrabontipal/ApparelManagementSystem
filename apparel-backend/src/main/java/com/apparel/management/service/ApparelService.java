package com.apparel.management.service;

import com.apparel.management.model.Apparel;
import com.apparel.management.repository.ApparelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApparelService {
    
    private final ApparelRepository apparelRepository;
    
    @Autowired
    public ApparelService(ApparelRepository apparelRepository) {
        this.apparelRepository = apparelRepository;
    }
    
    public List<Apparel> getAllApparels() {
        return apparelRepository.findAll();
    }
    
    public Optional<Apparel> getApparelById(Long id) {
        return apparelRepository.findById(id);
    }
    
    public Optional<Apparel> getApparelByName(String name) {
        return apparelRepository.findByName(name);
    }
    
    public Apparel saveApparel(Apparel apparel) {
        return apparelRepository.save(apparel);
    }
    
    public void deleteApparel(Long id) {
        apparelRepository.deleteById(id);
    }
}
