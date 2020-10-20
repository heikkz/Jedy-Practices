package ru.heikkz.jp.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.exception.CoreException;
import ru.heikkz.jp.model.RefreshToken;
import ru.heikkz.jp.repository.RefreshTokenRepository;
import ru.heikkz.jp.service.RefreshTokenService;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateToken(String token) {
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new CoreException("Невалидный рефреш токен"));
    }

    @Override
    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
