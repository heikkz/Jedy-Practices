package ru.heikkz.jp.service;

import ru.heikkz.jp.dto.HabitDto;
import ru.heikkz.jp.entity.habit.Habit;

import java.util.List;

public interface HabitService {

    /**
     * Добавить новую задачу
     */
    Habit create(HabitDto habitDto);

    /**
     * Получить список всех задач пользователя
     */
    List<Habit> findAllUserHabit();

    /**
     * Обновить информацию о таске
     */
    Habit update(HabitDto dto);
}
