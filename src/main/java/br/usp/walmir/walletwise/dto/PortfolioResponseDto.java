package br.usp.walmir.walletwise.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortfolioResponseDto {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
}

