package br.usp.walmir.walletwise.repository;

import br.usp.walmir.walletwise.model.Asset;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByPortfolioId(Long portfolioId);
}

