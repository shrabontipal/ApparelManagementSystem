package com.apparel.management.repository;

import com.apparel.management.model.Apparel;
import com.apparel.management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByApparel(Apparel apparel);
    List<Transaction> findByApparelId(Long apparelId);
    List<Transaction> findByType(Transaction.TransactionType type);
}
