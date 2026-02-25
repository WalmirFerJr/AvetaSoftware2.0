package br.usp.walmir.walletwise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedIncomeRequestDto {

    @NotNull
    private Long portfolioId;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal principal;

    /**
     * Taxa de juros mensal em formato decimal (ex: 0.01 = 1% ao mês).
     */
    @NotNull
    @Positive
    private BigDecimal monthlyRate;

    @NotNull
    private LocalDate startDate;

    private LocalDate maturityDate;
}

