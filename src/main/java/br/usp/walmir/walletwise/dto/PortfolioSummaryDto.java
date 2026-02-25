package br.usp.walmir.walletwise.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortfolioSummaryDto {

    private Long portfolioId;
    private BigDecimal totalVariableIncome;
    private BigDecimal totalFixedIncome;
    private BigDecimal totalInvested;
    private Long numberOfAssets;
    private Long numberOfFixedIncomes;
}

