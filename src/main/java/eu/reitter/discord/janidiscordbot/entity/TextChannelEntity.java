package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "TEXT_CHANNEL")
@Entity
public class TextChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEXT_CHANNEL")
    @SequenceGenerator(sequenceName = "SEQ_TEXT_CHANNEL", allocationSize = 1, name = "SEQ_TEXT_CHANNEL")
    private Long id;

    @Column(unique = true, nullable = false)
    private Long serverId;

    @Column(unique = true, nullable = false)
    private Long textChannelId;

}
