package ru.heikkz.jp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.heikkz.jp.dto.TaskDto;
import ru.heikkz.jp.entity.task.Task;
import ru.heikkz.jp.service.TaskService;

import javax.validation.Valid;
import java.util.List;

/**
 * REST контроллер для работы с задачами
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Добавить задачу
     * @param dto задача
     * @return добавленная задача
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER')")
    public Task createTask(@RequestBody @Valid TaskDto dto) {
        return taskService.create(dto);
    }

    @PostMapping("/update")
    public Task updateTask(@RequestBody @Valid TaskDto dto) {
        return taskService.update(dto);
    }

    /**
     * TODO добавить постраничность
     * Получить список всех задач пользователя
     * @return список всех задач пользователя
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public List<Task> getAllUserTasks() {
        return taskService.findAllUserTasks();
    }
}
