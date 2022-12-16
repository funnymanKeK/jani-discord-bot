package eu.reitter.discord.janidiscordbot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;



@Data
@Table(name = "COMPLIMENT")
@Entity
public class ComplimentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "complimentIdSeq")
    @SequenceGenerator(name = "complimentIdSeq", sequenceName = "SEQ_COMPLIMENT")
    private Long id;

    @Column(unique = true, nullable = false)
    private String compliment;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp modifyDate;

    @Column(nullable = false)
    private String createUser;

    @Column(nullable = false)
    private String modifyUser;


}
