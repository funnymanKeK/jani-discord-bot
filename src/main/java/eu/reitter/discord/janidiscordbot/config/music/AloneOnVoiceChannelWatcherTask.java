package eu.reitter.discord.janidiscordbot.config.music;

import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.exception.BotException;
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

    @Scheduled(fixedRateString = "${jani.discord.bot.music.alone.timer}")
    public void disconnectBotIfAlone() {
        log.debug("Start AloneOnVoiceChannelWatcherTask process");
        for (Server server : audioManager.servers) {
            if (server.getAudioConnection().isPresent()) {
                AudioConnection audioConnection = server.getAudioConnection().orElseThrow(() -> new BotException("No audio connection found!"));
                if (audioConnection.getChannel().getConnectedUsers().size() == 1) {
                    ServerMusicManager serverMusicManager = audioManager.get(server.getId());
                    serverMusicManager.player.stopTrack();
                    serverMusicManager.scheduler.clearQueue();
                    audioConnection.close();
                }
            }
        }
        log.debug("Finish AloneOnVoiceChannelWatcherTask process");
    }

}
