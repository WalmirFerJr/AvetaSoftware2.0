package br.usp.walmir.walletwise.controller;

import br.usp.walmir.walletwise.dto.FixedIncomeRequestDto;
import br.usp.walmir.walletwise.dto.FixedIncomeResponseDto;
import br.usp.walmir.walletwise.dto.FixedIncomeSimulationDto;
import br.usp.walmir.walletwise.service.FixedIncomeService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixed-incomes")
@RequiredArgsConstructor
public class FixedIncomeController {

    private final FixedIncomeService fixedIncomeService;

    @PostMapping
    public ResponseEntity<FixedIncomeResponseDto> create(@Valid @RequestBody FixedIncomeRequestDto request) {
        FixedIncomeResponseDto response = fixedIncomeService.createFixedIncome(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<FixedIncomeResponseDto>> listByPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(fixedIncomeService.listByPortfolio(portfolioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FixedIncomeResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FixedIncomeRequestDto request) {
        return ResponseEntity.ok(fixedIncomeService.updateFixedIncome(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fixedIncomeService.deleteFixedIncome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/simulation")
    public ResponseEntity<FixedIncomeSimulationDto> simulate(
            @PathVariable Long id,
            @RequestParam(name = "months") int months) {
        return ResponseEntity.ok(fixedIncomeService.simulate(id, months));
    }
}

