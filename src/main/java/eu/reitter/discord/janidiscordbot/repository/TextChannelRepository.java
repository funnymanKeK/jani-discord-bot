package eu.reitter.discord.janidiscordbot.repository;

import eu.reitter.discord.janidiscordbot.entity.TextChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextChannelRepository extends JpaRepository<TextChannelEntity, Long> {
}
