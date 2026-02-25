package br.usp.walmir.walletwise.service;

import br.usp.walmir.walletwise.dto.TransactionResponseDto;
import br.usp.walmir.walletwise.model.AssetType;
import br.usp.walmir.walletwise.model.Portfolio;
import br.usp.walmir.walletwise.model.Transaction;
import br.usp.walmir.walletwise.model.TransactionType;
import br.usp.walmir.walletwise.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction logVariableIncomeBuy(
            Portfolio portfolio,
            String ticker,
            int quantity,
            BigDecimal price) {

        Transaction transaction = Transaction.builder()
                .portfolio(portfolio)
                .type(TransactionType.BUY)
                .assetType(AssetType.VARIABLE_INCOME)
                .assetTicker(ticker)
                .quantity(quantity)
                .amount(price.multiply(BigDecimal.valueOf(quantity)))
                .transactionDate(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    public Transaction logFixedIncomeDeposit(
            Portfolio portfolio,
            String name,
            BigDecimal principal) {

        Transaction transaction = Transaction.builder()
                .portfolio(portfolio)
                .type(TransactionType.DEPOSIT)
                .assetType(AssetType.FIXED_INCOME)
                .assetTicker(name)
                .quantity(null)
                .amount(principal)
                .transactionDate(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    public List<TransactionResponseDto> listTransactions(Long portfolioId, LocalDateTime from, LocalDateTime to) {
        return transactionRepository.findByPortfolioAndPeriod(portfolioId, from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TransactionResponseDto toDto(Transaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .portfolioId(transaction.getPortfolio().getId())
                .type(transaction.getType())
                .assetType(transaction.getAssetType())
                .assetTicker(transaction.getAssetTicker())
                .quantity(transaction.getQuantity())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}

