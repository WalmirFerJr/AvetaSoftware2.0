package br.usp.walmir.walletwise.controller;

import br.usp.walmir.walletwise.dto.PortfolioResponseDto;
import br.usp.walmir.walletwise.dto.UserRequestDto;
import br.usp.walmir.walletwise.dto.UserResponseDto;
import br.usp.walmir.walletwise.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/portfolio")
    public ResponseEntity<PortfolioResponseDto> getUserPortfolio(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserPortfolio(id));
    }
}

