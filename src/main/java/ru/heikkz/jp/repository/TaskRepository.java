package ru.heikkz.jp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import ru.heikkz.jp.entity.task.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select name from Task where user.id=:userId")
    List<String> findAllTaskNameByUserId(@RequestParam Long userId);

    @Query("from Task where user.id=:userId")
    List<Task> findAllTasksByUserId(@RequestParam Long userId);
}
