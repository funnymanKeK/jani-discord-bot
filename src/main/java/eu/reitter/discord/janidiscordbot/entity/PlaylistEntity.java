package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "PLAYLIST")
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLAYLIST")
    @SequenceGenerator(sequenceName = "SEQ_PLAYLIST", allocationSize = 1, name = "SEQ_PLAYLIST")
    private Long id;

    @Column(unique = true)
    private String name;

    @JoinTable(
            name = "PLAYLIST_TRACK",
            joinColumns = @JoinColumn(
                    name = "PLAYLIST_ID",
                    referencedColumnName = "ID"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "TRACK_ID",
                    referencedColumnName = "ID"
            )
    )
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrackEntity> tracks;

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
        PlaylistEntity that = (PlaylistEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
