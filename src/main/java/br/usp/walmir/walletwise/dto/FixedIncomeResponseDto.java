package br.usp.walmir.walletwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FixedIncomeResponseDto {

    private Long id;
    private Long portfolioId;
    private String name;
    private BigDecimal principal;
    private BigDecimal monthlyRate;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private LocalDateTime createdAt;
}

