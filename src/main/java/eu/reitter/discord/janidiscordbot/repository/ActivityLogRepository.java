package eu.reitter.discord.janidiscordbot.repository;

import eu.reitter.discord.janidiscordbot.entity.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {

    Optional<ActivityLogEntity> findByMessageId(long messageId);

}
