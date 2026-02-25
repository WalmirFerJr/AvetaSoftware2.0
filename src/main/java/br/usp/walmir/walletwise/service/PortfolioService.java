package br.usp.walmir.walletwise.service;

import br.usp.walmir.walletwise.dto.PortfolioSummaryDto;
import br.usp.walmir.walletwise.exception.ResourceNotFoundException;
import br.usp.walmir.walletwise.model.Asset;
import br.usp.walmir.walletwise.model.FixedIncome;
import br.usp.walmir.walletwise.model.Portfolio;
import br.usp.walmir.walletwise.repository.AssetRepository;
import br.usp.walmir.walletwise.repository.FixedIncomeRepository;
import br.usp.walmir.walletwise.repository.PortfolioRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    private final FixedIncomeRepository fixedIncomeRepository;

    public PortfolioSummaryDto getSummary(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));

        List<Asset> assets = assetRepository.findByPortfolioId(portfolio.getId());
        List<FixedIncome> fixedIncomes = fixedIncomeRepository.findByPortfolioId(portfolio.getId());

        BigDecimal totalVariable = assets.stream()
                .map(a -> a.getAveragePrice().multiply(BigDecimal.valueOf(a.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFixed = fixedIncomes.stream()
                .map(FixedIncome::getPrincipal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = totalVariable.add(totalFixed);

        return PortfolioSummaryDto.builder()
                .portfolioId(portfolio.getId())
                .totalVariableIncome(totalVariable)
                .totalFixedIncome(totalFixed)
                .totalInvested(total)
                .numberOfAssets((long) assets.size())
                .numberOfFixedIncomes((long) fixedIncomes.size())
                .build();
    }
}

