package br.usp.walmir.walletwise.controller;

import br.usp.walmir.walletwise.dto.PortfolioSummaryDto;
import br.usp.walmir.walletwise.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/{id}/summary")
    public ResponseEntity<PortfolioSummaryDto> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.getSummary(id));
    }
}

