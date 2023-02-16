package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "TRACK")
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRACK")
    @SequenceGenerator(sequenceName = "SEQ_TRACK", allocationSize = 1, name = "SEQ_TRACK")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private PlaylistEntity playlist;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp modifyDate;

    @Column(nullable = false)
    private String createUser;

    @Column(nullable = false)
    private String modifyUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrackEntity that = (TrackEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
