package br.usp.walmir.walletwise.repository;

import br.usp.walmir.walletwise.model.FixedIncome;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedIncomeRepository extends JpaRepository<FixedIncome, Long> {

    List<FixedIncome> findByPortfolioId(Long portfolioId);
}

