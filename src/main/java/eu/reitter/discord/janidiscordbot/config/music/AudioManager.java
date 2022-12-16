package eu.reitter.discord.janidiscordbot.config.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.server.Server;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AudioManager {

    private final Map<Long, ServerMusicManager> managers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager;
    public final Set<Server> servers = new HashSet<>();

    public ServerMusicManager get(long server) {

        if (!managers.containsKey(server)) {
            managers.put(server, new ServerMusicManager(audioPlayerManager));
        }

        return managers.get(server);
    }

}
