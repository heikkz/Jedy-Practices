package ru.heikkz.jp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import ru.heikkz.jp.entity.habit.Habit;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    @Query("select name from Habit where user.id=:userId")
    List<String> findAllHabitNameByUserId(@RequestParam Long userId);

    @Query("from Habit where user.id=:userId")
    List<Habit> findAllHabitsByUserId(@RequestParam Long userId);
}
