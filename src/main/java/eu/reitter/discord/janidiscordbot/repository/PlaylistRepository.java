package eu.reitter.discord.janidiscordbot.repository;

import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

    Optional<PlaylistEntity> findByName(String name);

}
