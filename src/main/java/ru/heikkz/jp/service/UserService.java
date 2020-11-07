package ru.heikkz.jp.service;

import org.springframework.data.domain.Page;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.task.Task;

import java.util.List;

public interface UserService {

    User getCurrentUser();

    User update(User user);

    User save(User user);

    void delete(Long id);
}
