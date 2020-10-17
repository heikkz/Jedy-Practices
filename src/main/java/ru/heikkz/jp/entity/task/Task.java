package ru.heikkz.jp.entity.task;

import lombok.*;
import ru.heikkz.jp.entity.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "tasks")
public class Task {

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
    @CollectionTable(name = "task_dates_mapping",
            joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "task_date")
    @Column(name = "complete")
    private Map<LocalDate, Boolean> dateToCompleteMap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
