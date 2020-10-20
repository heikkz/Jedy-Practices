package ru.heikkz.jp.service;

import org.springframework.data.domain.Page;
import ru.heikkz.jp.entity.User;

import java.util.List;

public interface UserService {

    User update(User user);

    User findById(Long id);

    void delete(Long id);

    List<User> findAll();

    Page<User> findAll(int page, int count);
}
