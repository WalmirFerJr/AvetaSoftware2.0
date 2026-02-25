package br.usp.walmir.walletwise.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private Long portfolioId;
    private LocalDateTime createdAt;
}

