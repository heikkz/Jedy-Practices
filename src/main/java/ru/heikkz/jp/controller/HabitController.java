package ru.heikkz.jp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.heikkz.jp.dto.HabitDto;
import ru.heikkz.jp.entity.habit.Habit;
import ru.heikkz.jp.service.HabitService;

import javax.validation.Valid;
import java.util.List;

/**
 * REST контроллер для работы с привычками
 */
@RestController
@RequestMapping("/api/v1/habits")
public class HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    /**
     * Добавить задачу
     * @param dto задача
     * @return добавленная задача
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER')")
    public Habit createHabit(@RequestBody @Valid HabitDto dto) {
        return habitService.create(dto);
    }

    @PostMapping("/update")
    public Habit updateHabit(@RequestBody @Valid HabitDto dto) {
        return habitService.update(dto);
    }

    /**
     * TODO добавить постраничность
     * Получить список всех задач пользователя
     * @return список всех задач пользователя
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public List<Habit> getAllUserHabits() {
        return habitService.findAllUserHabit();
    }
}
