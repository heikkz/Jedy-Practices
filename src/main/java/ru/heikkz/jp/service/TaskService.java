package ru.heikkz.jp.service;

import ru.heikkz.jp.dto.TaskDto;
import ru.heikkz.jp.entity.task.Task;

import java.util.List;

public interface TaskService {

    /**
     * Добавить новую задачу
     */
    Task create(TaskDto taskDto);

    /**
     * Получить список всех задач пользователя
     */
    List<Task> findAllUserTasks();

    /**
     * Обновить информацию о таске
     */
    Task update(TaskDto dto);
}
