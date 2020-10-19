package ru.heikkz.jp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.heikkz.jp.entity.auth.VerificationToken;

import java.util.Optional;

/**
 * Репозиторий для работы с токенами верификации регистрации пользователя
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    /**
     * Получить объект токена по сгенерированному значению
     * @param token сгенерированный токен
     * @return токен, или {@link Optional#empty()} если ничего не найдено
     */
    Optional<VerificationToken> findByToken(String token);
}
