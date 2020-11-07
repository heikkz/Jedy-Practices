package ru.heikkz.jp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.dto.HabitDto;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.habit.Habit;
import ru.heikkz.jp.entity.habit.Status;
import ru.heikkz.jp.entity.habit.Type;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.repository.HabitRepository;
import ru.heikkz.jp.service.HabitService;
import ru.heikkz.jp.service.UserService;

import java.util.List;

@Service
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final UserService userService;

    @Autowired
    public HabitServiceImpl(HabitRepository habitRepository, UserService userService) {
        this.habitRepository = habitRepository;
        this.userService = userService;
    }

    /**
     * Создать новую задачу
     * @param habitDto задача
     * @return задача
     */
    @Transactional
    @Override
    public Habit create(HabitDto habitDto) {
        User currentUser = userService.getCurrentUser();
        String name = habitDto.getName();
        if (currentUser.getHabits().stream().anyMatch(t -> t.getName().equals(name))) {
            throw new MyBadRequestException("Задача с названием '" + name + "' уже существует");
        }

        Habit habit = new Habit();
        habit.setName(name);
        habit.setDescription(habitDto.getDescription());
        habit.setType(Type.valueOf(habitDto.getType()));
        habit.setStatus(Status.ACTIVE);
        habit.setUser(currentUser);
        Habit savedHabit = habitRepository.save(habit);
        currentUser.getHabits().add(savedHabit);
        userService.save(currentUser);
        return savedHabit;
    }

    /**
     * Получить список всех задач пользователя
     * @return список всех задач пользователя
     */
    @Transactional(readOnly = true)
    @Override
    public List<Habit> findAllUserHabit() {
        User currentUser = userService.getCurrentUser();
        return habitRepository.findAllHabitsByUserId(currentUser.getId());
    }

    @Override
    public Habit update(HabitDto dto) {
        return null;
    }
}
