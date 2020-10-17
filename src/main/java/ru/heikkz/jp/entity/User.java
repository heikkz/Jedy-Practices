package ru.heikkz.jp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.heikkz.jp.entity.task.Task;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

/**
 * Сущность пользователя в системе
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Логин пользователя
     */
    @Size(min = 3, message = "Name must be at least 3 characters!")
    @Column(nullable = false)
    private String name;
    /**
     * Почтовый адрес
     */
    @Size(min = 1, message = "Invalid email address!")
    @Email(message = "Invalid email address!")
    private String email;
    /**
     * Пароль
     */
    @Size(min = 5, message = "Password must be at least 5 characters!")
    @JsonIgnore
    private String password;

//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private AuthProvider provider;

//    private String providerId;

    /**
     * Дата регистрации
     */
    private Instant created;
    /**
     * Признак что пользователь активен
     */
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany
    @JoinTable(name="user_roles")
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    private boolean admin;
}
