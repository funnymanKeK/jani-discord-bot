package eu.reitter.discord.janidiscordbot.command.music.playlist;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.LavaplayerAudioSource;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.entity.TrackEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IPlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import javax.sound.midi.Track;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayPlaylistCommand implements ICommand {

    private final Properties properties;

    private final AudioManager audioManager;
    private final IPlaylistService playlistService;
    private final AudioPlayerManager audioPlayerManager;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        log.debug("Begin playPlaylist command with arguments {}", Arrays.toString(arguments));

        if (badArguments(event, arguments, 1, true, null)) {
            log.debug("Arguments does not match!");
            return;
        }

        String playlistName = arguments[0];
        log.debug("PlaylistName: {}", playlistName);

        PlaylistEntity playlist = playlistService.findByName(playlistName);
        if (playlist.getTracks().isEmpty()) throw new BotException("Playlist does not contain any tracks!");
        final TextChannel textChannel = event.getChannel();

        if (!isAuthorOnVoiceChannel(event)) {
            textChannel.sendMessage(createSimpleEmbedMessage("You are not connected to any voice channels!"));
            return;
        }

        final ServerVoiceChannel voiceChannel = event.getMessageAuthor().getConnectedVoiceChannel().orElseThrow(() -> new BotException("No voice channel found!"));

        if (!voiceChannel.canYouSee() || !voiceChannel.canYouConnect()) {
            textChannel.sendMessage(createSimpleEmbedMessage("The bot cannot see or connect to the voice channel!"));
            return;
        }

        if (!voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK)) {
            textChannel.sendMessage(createSimpleEmbedMessage("The bot does not have permission to speak!"));
            return;
        }

        final Server server = event.getServer().orElseThrow(() -> new BotException("No server found!"));
        final ServerMusicManager serverMusicManager = audioManager.get(server.getId());
        audioManager.servers.add(server);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Playing %s playlist...", playlistName));
        embedBuilder.setColor(randomColor());

        if (!isBotConnected(event, voiceChannel) && server.getAudioConnection().isEmpty()) {
            voiceChannel.connect().thenAccept(audioConnection -> {
                        AudioSource audio = new LavaplayerAudioSource(event.getApi(), serverMusicManager.player);
                        audioConnection.setAudioSource(audio);
                        audioConnection.setSelfDeafened(true);
                        try {
                            for (int i = 0; i < playlist.getTracks().size(); i++) {
                                String title = playMusic(playlist.getTracks().get(i).getUrl(), serverMusicManager, audioPlayerManager).get();
                                embedBuilder.addField(i + ".", title);
                            }
                            textChannel.sendMessage(embedBuilder);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                        }
                    }
            );
        }

        //Bot is connected to voice channel
        if (server.getAudioConnection().isPresent()) {
            final AudioConnection audioConnection = server.getAudioConnection().orElseThrow(() -> new BotException("No audio connection found!"));

            if (audioConnection.getChannel().getId() != voiceChannel.getId()) {
                textChannel.sendMessage(createSimpleEmbedMessage("You are not on the same channel as the bot!"));
                return;
            }

            AudioSource audio = new LavaplayerAudioSource(event.getApi(), serverMusicManager.player);
            audioConnection.setAudioSource(audio);
            audioConnection.setSelfDeafened(true);
            try {
                for (int i = 0; i < playlist.getTracks().size(); i++) {
                    String title = playMusic(playlist.getTracks().get(i).getUrl(), serverMusicManager, audioPlayerManager).get();
                    embedBuilder.addField(i + ".", title);
                }
                textChannel.sendMessage(embedBuilder);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BotException(e);
            } catch (ExecutionException e) {
                throw new BotException(e);
            }
        }
        log.debug("PlayPlaylist command ended");
    }

    @Override
    public String getPrefix() {
        return "playPlaylist";
    }

    @Override
    public String getDescription() {
        return String.format("Queues the tracks of the given playlist. Usage: %s%s 'playlist name'", properties.getPrefix(), getPrefix());
    }
}
