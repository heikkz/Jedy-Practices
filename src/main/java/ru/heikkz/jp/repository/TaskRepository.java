package ru.heikkz.jp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.heikkz.jp.entity.task.Task;
import ru.heikkz.jp.entity.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUser(User user);

    Page<Task> findAllByUser(User user, PageRequest pageRequest);
}
