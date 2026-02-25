package br.usp.walmir.walletwise.service;

import br.usp.walmir.walletwise.dto.PortfolioResponseDto;
import br.usp.walmir.walletwise.dto.UserRequestDto;
import br.usp.walmir.walletwise.dto.UserResponseDto;
import br.usp.walmir.walletwise.exception.ResourceNotFoundException;
import br.usp.walmir.walletwise.model.Portfolio;
import br.usp.walmir.walletwise.model.User;
import br.usp.walmir.walletwise.repository.PortfolioRepository;
import br.usp.walmir.walletwise.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public UserResponseDto createUser(UserRequestDto request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);

        Portfolio portfolio = Portfolio.builder()
                .name("Carteira Padrão")
                .user(savedUser)
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        savedUser.setPortfolio(savedPortfolio);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .portfolioId(savedPortfolio.getId())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Long portfolioId = user.getPortfolio() != null ? user.getPortfolio().getId() : null;

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .portfolioId(portfolioId)
                .createdAt(user.getCreatedAt())
                .build();
    }

    public PortfolioResponseDto getUserPortfolio(Long userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada para o usuário"));

        return PortfolioResponseDto.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .createdAt(portfolio.getCreatedAt())
                .build();
    }
}

