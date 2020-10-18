package ru.heikkz.jp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.heikkz.jp.entity.Role;
import ru.heikkz.jp.entity.User;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// TODO дописать тесты
@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void assertTrue_whenUserExists() {
        Optional<User> byEmail = userRepository.findByEmail("testuser@mail.ru");
        assertTrue(byEmail.isPresent());
    }

    @Test
    public void whenCreateUser_thenSuccess() {
        User saved = userRepository.save(getTestUser());
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    private User getTestUser() {
        User user = new User();
        user.setCreated(LocalDate.of(2020, 10, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        user.setEmail("user@mail.ru");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setRole(Role.USER);
        return user;
    }
}
