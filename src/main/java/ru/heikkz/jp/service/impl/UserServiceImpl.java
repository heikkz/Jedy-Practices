package ru.heikkz.jp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.task.Task;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.repository.UserRepository;
import ru.heikkz.jp.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + principal.getUsername()));
    }

    @Override
    public User update(User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        } else {
            User dbUser = optional.get();
            if (!dbUser.getId().equals(user.getId())) {
                // TODO добавить логгирование
                throw new MyBadRequestException("Неверные данные");
            }
            return userRepository.save(user);
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {

    }
}
