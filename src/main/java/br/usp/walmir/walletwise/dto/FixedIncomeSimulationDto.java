package br.usp.walmir.walletwise.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FixedIncomeSimulationDto {

    private Long fixedIncomeId;
    private BigDecimal principal;
    private BigDecimal monthlyRate;
    private Integer months;
    private BigDecimal projectedAmount;
}

