package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "TRACK")
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackIdSeq")
    @SequenceGenerator(name = "trackIdSeq", sequenceName = "SEQ_TRACK")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PlaylistEntity playlistDTO;

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

}
