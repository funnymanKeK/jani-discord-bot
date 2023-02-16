package eu.reitter.discord.janidiscordbot.repository;

import eu.reitter.discord.janidiscordbot.entity.ComplimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplimentRepository extends JpaRepository<ComplimentEntity, Long> {

    @Query(value = "SELECT * FROM (SELECT * FROM COMPLIMENT ORDER BY DBMS_RANDOM.RANDOM) WHERE  rownum < :limit", nativeQuery = true)
    List<ComplimentEntity> findRandomCompliment(@Param("limit") int limit);

}
