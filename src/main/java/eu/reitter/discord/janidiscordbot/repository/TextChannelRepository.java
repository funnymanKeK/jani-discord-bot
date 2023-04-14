package eu.reitter.discord.janidiscordbot.repository;

import eu.reitter.discord.janidiscordbot.entity.TextChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextChannelRepository extends JpaRepository<TextChannelEntity, Long> {

    Optional<TextChannelEntity> findByTextChannelId(Long textChannelId);
    Optional<TextChannelEntity> findByServerId(Long serverId);

}
