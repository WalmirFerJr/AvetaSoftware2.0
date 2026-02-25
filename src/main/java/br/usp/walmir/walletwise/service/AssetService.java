package br.usp.walmir.walletwise.service;

import br.usp.walmir.walletwise.dto.AssetRequestDto;
import br.usp.walmir.walletwise.dto.AssetResponseDto;
import br.usp.walmir.walletwise.exception.ResourceNotFoundException;
import br.usp.walmir.walletwise.model.Asset;
import br.usp.walmir.walletwise.model.Portfolio;
import br.usp.walmir.walletwise.repository.AssetRepository;
import br.usp.walmir.walletwise.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionService transactionService;

    @Transactional
    public AssetResponseDto createAsset(AssetRequestDto request) {
        Portfolio portfolio = portfolioRepository.findById(request.getPortfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));

        LocalDateTime now = LocalDateTime.now();

        Asset asset = Asset.builder()
                .portfolio(portfolio)
                .ticker(request.getTicker().toUpperCase())
                .quantity(request.getQuantity())
                .averagePrice(request.getPrice())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Asset saved = assetRepository.save(asset);

        transactionService.logVariableIncomeBuy(
                portfolio,
                saved.getTicker(),
                saved.getQuantity(),
                saved.getAveragePrice());

        return toDto(saved);
    }

    public List<AssetResponseDto> listByPortfolio(Long portfolioId) {
        return assetRepository.findByPortfolioId(portfolioId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssetResponseDto updateAsset(Long id, AssetRequestDto request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ativo não encontrado"));

        if (!asset.getPortfolio().getId().equals(request.getPortfolioId())) {
            throw new IllegalArgumentException("O ativo não pertence à carteira informada");
        }

        asset.setTicker(request.getTicker().toUpperCase());
        asset.setQuantity(request.getQuantity());
        asset.setAveragePrice(request.getPrice());
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);
        return toDto(saved);
    }

    @Transactional
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ativo não encontrado"));
        assetRepository.delete(asset);
    }

    private AssetResponseDto toDto(Asset asset) {
        BigDecimal positionValue = asset.getAveragePrice()
                .multiply(BigDecimal.valueOf(asset.getQuantity()));

        return AssetResponseDto.builder()
                .id(asset.getId())
                .portfolioId(asset.getPortfolio().getId())
                .ticker(asset.getTicker())
                .quantity(asset.getQuantity())
                .averagePrice(asset.getAveragePrice())
                .positionValue(positionValue)
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }
}

