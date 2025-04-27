package com.apparel.management.service;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Transaction;
import com.apparel.management.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    public List<Transaction> getTransactionsByApparel(Apparel apparel) {
        return transactionRepository.findByApparel(apparel);
    }
    
    public List<Transaction> getTransactionsByApparelId(Long apparelId) {
        return transactionRepository.findByApparelId(apparelId);
    }
    
    public List<Transaction> getTransactionsByType(Transaction.TransactionType type) {
        return transactionRepository.findByType(type);
    }
    
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    public double calculateProfit(Long apparelId) {
        List<Transaction> transactions = transactionRepository.findByApparelId(apparelId);
        
        double totalProfit = 0.0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.PURCHASE) {
                totalProfit += transaction.getAmount();
            } else if (transaction.getType() == Transaction.TransactionType.REFUND) {
                totalProfit -= transaction.getAmount();
            }
        }
        
        return totalProfit;
    }
}
