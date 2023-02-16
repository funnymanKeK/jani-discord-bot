package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "ACTIVITY_LOG")
@Entity
public class ActivityLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACTIVITY_LOG")
    @SequenceGenerator(sequenceName = "SEQ_ACTIVITY_LOG", allocationSize = 1, name = "SEQ_ACTIVITY_LOG")
    private Long id;
    @Column(nullable = false)
    private String commandName;
    @Column
    private String arguments;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long messageId;
    @Column(nullable = false)
    private String userName;
    @Column
    private Timestamp startDate;
    @Column
    private Timestamp finishDate;
    @Column
    private String errorMessage;
    @Column
    private Long serverId;
    @Column
    private String serverName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivityLogEntity that = (ActivityLogEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
