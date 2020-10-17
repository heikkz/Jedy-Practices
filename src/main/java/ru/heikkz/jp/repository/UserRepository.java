package ru.heikkz.jp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.heikkz.jp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
