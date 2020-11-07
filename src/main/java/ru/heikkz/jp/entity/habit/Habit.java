package ru.heikkz.jp.entity.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.heikkz.jp.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    // TODO валидатор. Проверка уникальности наименования для клиента
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @ElementCollection
    @CollectionTable(name="habit_checks", joinColumns=@JoinColumn(name = "habit_id", referencedColumnName = "id"))
    private List<Check> checks = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
