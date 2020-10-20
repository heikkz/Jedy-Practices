package ru.heikkz.jp.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация {@link UserDetailsService} c использованием БД, через {@link UserRepository}
 */
@Service
@AllArgsConstructor
@Primary
public class CustomUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Получить сведения о пользователе для Spring Security
     * @param email имя пользователя
     * @return {@link UserDetails} сведения о пользователе
     * @throws UsernameNotFoundException если пользователь не был найден
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(),
                true, true, true, getAuthorities(user.getRole().name()));
    }

    /**
     * Создать список с переданной ролью в системе
     * @param role роль пользователя
     * @return список с переданной ролью в системе
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
