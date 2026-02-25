package br.usp.walmir.walletwise.dto;

import br.usp.walmir.walletwise.model.AssetType;
import br.usp.walmir.walletwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponseDto {

    private Long id;
    private Long portfolioId;
    private TransactionType type;
    private AssetType assetType;
    private String assetTicker;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
}

