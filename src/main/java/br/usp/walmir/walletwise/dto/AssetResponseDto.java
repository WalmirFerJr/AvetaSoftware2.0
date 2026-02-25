package br.usp.walmir.walletwise.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetResponseDto {

    private Long id;
    private Long portfolioId;
    private String ticker;
    private Integer quantity;
    private BigDecimal averagePrice;
    private BigDecimal positionValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

