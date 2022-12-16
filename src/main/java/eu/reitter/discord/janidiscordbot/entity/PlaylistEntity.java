package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "PLAYLIST")
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlistIdSeq")
    @SequenceGenerator(name = "playlistIdSeq", sequenceName = "SEQ_PLAYLIST")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp modifyDate;

    @Column(nullable = false)
    private String createUser;

    @Column(nullable = false)
    private String modifyUser;

}
