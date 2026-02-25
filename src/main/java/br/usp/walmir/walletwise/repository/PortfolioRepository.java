package br.usp.walmir.walletwise.repository;

import br.usp.walmir.walletwise.model.Portfolio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUserId(Long userId);
}

