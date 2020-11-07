package ru.heikkz.jp.service;

import ru.heikkz.jp.entity.User;

public interface UserService {

    User getCurrentUser();

    User update(User user);

    User save(User user);

    void delete(Long id);
}
