package ru.heikkz.jp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.dto.TaskDto;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.task.Status;
import ru.heikkz.jp.entity.task.Task;
import ru.heikkz.jp.entity.task.Type;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.repository.TaskRepository;
import ru.heikkz.jp.service.TaskService;
import ru.heikkz.jp.service.UserService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    /**
     * Создать новую задачу
     * @param taskDto задача
     * @return задача
     */
    @Transactional
    @Override
    public Task create(TaskDto taskDto) {
        User currentUser = userService.getCurrentUser();
        String name = taskDto.getName();
        if (currentUser.getTasks().stream().anyMatch(t -> t.getName().equals(name))) {
            throw new MyBadRequestException("Задача с названием '" + name + "' уже существует");
        }

        Task task = new Task();
        task.setName(name);
        task.setDescription(taskDto.getDescription());
        task.setType(Type.valueOf(taskDto.getType()));
        task.setStatus(Status.ACTIVE);
        task.setUser(currentUser);
        Task savedTask = taskRepository.save(task);
        currentUser.getTasks().add(savedTask);
        userService.save(currentUser);
        return savedTask;
    }

    /**
     * Получить список всех задач пользователя
     * @return список всех задач пользователя
     */
    @Transactional(readOnly = true)
    @Override
    public List<Task> findAllUserTasks() {
        User currentUser = userService.getCurrentUser();
        return taskRepository.findAllTasksByUserId(currentUser.getId());
    }

    @Override
    public Task update(TaskDto dto) {
        return null;
    }
}
