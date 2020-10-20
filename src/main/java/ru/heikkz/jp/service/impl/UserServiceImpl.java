package ru.heikkz.jp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.heikkz.jp.entity.User;
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
    public User findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(int page, int count) {
        return null;
    }
}
