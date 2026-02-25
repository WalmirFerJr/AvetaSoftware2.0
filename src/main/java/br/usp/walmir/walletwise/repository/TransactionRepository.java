package br.usp.walmir.walletwise.repository;

import br.usp.walmir.walletwise.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            select t from Transaction t
            where t.portfolio.id = :portfolioId
              and (:from is null or t.transactionDate >= :from)
              and (:to is null or t.transactionDate <= :to)
            order by t.transactionDate desc
            """)
    List<Transaction> findByPortfolioAndPeriod(
            @Param("portfolioId") Long portfolioId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}

