package br.usp.walmir.walletwise.controller;

import br.usp.walmir.walletwise.dto.AssetRequestDto;
import br.usp.walmir.walletwise.dto.AssetResponseDto;
import br.usp.walmir.walletwise.service.AssetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponseDto> create(@Valid @RequestBody AssetRequestDto request) {
        AssetResponseDto response = assetService.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<AssetResponseDto>> listByPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(assetService.listByPortfolio(portfolioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequestDto request) {
        return ResponseEntity.ok(assetService.updateAsset(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}

