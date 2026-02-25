package br.usp.walmir.walletwise.service;

import br.usp.walmir.walletwise.dto.FixedIncomeRequestDto;
import br.usp.walmir.walletwise.dto.FixedIncomeResponseDto;
import br.usp.walmir.walletwise.dto.FixedIncomeSimulationDto;
import br.usp.walmir.walletwise.exception.ResourceNotFoundException;
import br.usp.walmir.walletwise.model.FixedIncome;
import br.usp.walmir.walletwise.model.Portfolio;
import br.usp.walmir.walletwise.repository.FixedIncomeRepository;
import br.usp.walmir.walletwise.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixedIncomeService {

    private final FixedIncomeRepository fixedIncomeRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionService transactionService;

    @Transactional
    public FixedIncomeResponseDto createFixedIncome(FixedIncomeRequestDto request) {
        Portfolio portfolio = portfolioRepository.findById(request.getPortfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));

        FixedIncome fixedIncome = FixedIncome.builder()
                .portfolio(portfolio)
                .name(request.getName())
                .principal(request.getPrincipal())
                .monthlyRate(request.getMonthlyRate())
                .startDate(request.getStartDate())
                .maturityDate(request.getMaturityDate())
                .createdAt(LocalDateTime.now())
                .build();

        FixedIncome saved = fixedIncomeRepository.save(fixedIncome);

        transactionService.logFixedIncomeDeposit(
                portfolio,
                saved.getName(),
                saved.getPrincipal());

        return toDto(saved);
    }

    public List<FixedIncomeResponseDto> listByPortfolio(Long portfolioId) {
        return fixedIncomeRepository.findByPortfolioId(portfolioId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FixedIncomeResponseDto updateFixedIncome(Long id, FixedIncomeRequestDto request) {
        FixedIncome fixedIncome = fixedIncomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Renda fixa não encontrada"));

        if (!fixedIncome.getPortfolio().getId().equals(request.getPortfolioId())) {
            throw new IllegalArgumentException("O título não pertence à carteira informada");
        }

        fixedIncome.setName(request.getName());
        fixedIncome.setPrincipal(request.getPrincipal());
        fixedIncome.setMonthlyRate(request.getMonthlyRate());
        fixedIncome.setStartDate(request.getStartDate());
        fixedIncome.setMaturityDate(request.getMaturityDate());

        FixedIncome saved = fixedIncomeRepository.save(fixedIncome);
        return toDto(saved);
    }

    @Transactional
    public void deleteFixedIncome(Long id) {
        FixedIncome fixedIncome = fixedIncomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Renda fixa não encontrada"));
        fixedIncomeRepository.delete(fixedIncome);
    }

    public FixedIncomeSimulationDto simulate(Long fixedIncomeId, int months) {
        FixedIncome fixedIncome = fixedIncomeRepository.findById(fixedIncomeId)
                .orElseThrow(() -> new ResourceNotFoundException("Renda fixa não encontrada"));

        BigDecimal projected = simulateCompoundInterest(
                fixedIncome.getPrincipal(),
                fixedIncome.getMonthlyRate(),
                months);

        return FixedIncomeSimulationDto.builder()
                .fixedIncomeId(fixedIncome.getId())
                .principal(fixedIncome.getPrincipal())
                .monthlyRate(fixedIncome.getMonthlyRate())
                .months(months)
                .projectedAmount(projected)
                .build();
    }

    public BigDecimal simulateCompoundInterest(BigDecimal principal, BigDecimal monthlyRate, int months) {
        if (months < 0) {
            throw new IllegalArgumentException("Meses não pode ser negativo");
        }

        BigDecimal base = BigDecimal.ONE.add(monthlyRate);
        BigDecimal factor = BigDecimal.ONE;
        for (int i = 0; i < months; i++) {
            factor = factor.multiply(base);
        }

        return principal.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    private FixedIncomeResponseDto toDto(FixedIncome fixedIncome) {
        return FixedIncomeResponseDto.builder()
                .id(fixedIncome.getId())
                .portfolioId(fixedIncome.getPortfolio().getId())
                .name(fixedIncome.getName())
                .principal(fixedIncome.getPrincipal())
                .monthlyRate(fixedIncome.getMonthlyRate())
                .startDate(fixedIncome.getStartDate())
                .maturityDate(fixedIncome.getMaturityDate())
                .createdAt(fixedIncome.getCreatedAt())
                .build();
    }
}

