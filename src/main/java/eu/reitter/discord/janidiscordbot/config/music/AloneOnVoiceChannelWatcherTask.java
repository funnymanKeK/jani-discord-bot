package eu.reitter.discord.janidiscordbot.config.music;

import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.server.Server;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AloneOnVoiceChannelWatcherTask {

    private final AudioManager audioManager;

    @Scheduled(fixedRate = 30000)
    public void disconnectBotIfAlone() {
        log.info("Start AloneOnVoiceChannelWatcherTask process");
        for (Server server : audioManager.servers) {
            if (server.getAudioConnection().isPresent()) {
                AudioConnection audioConnection = server.getAudioConnection().get();
                if (audioConnection.getChannel().getConnectedUsers().size() == 1) {
                    ServerMusicManager serverMusicManager = audioManager.get(server.getId());
                    serverMusicManager.player.stopTrack();
                    serverMusicManager.scheduler.clearQueue();
                    audioConnection.close();
                }
            }
        }
        log.info("Finish AloneOnVoiceChannelWatcherTask process");
    }

}
