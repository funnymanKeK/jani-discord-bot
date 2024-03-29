package eu.reitter.discord.janidiscordbot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.LavaplayerAudioSource;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import lombok.RequiredArgsConstructor;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.*;

@Component
@RequiredArgsConstructor
public class PlayCommand implements ICommand {

    private final AudioPlayerManager audioPlayerManager;
    private final AudioManager audioManager;
    private final Properties properties;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) throws BotException {
        if (badArguments(event, arguments, 1, false, null)) return;
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

        //Bot is not connected to voice channel
        if (!isBotConnected(event, voiceChannel) && server.getAudioConnection().isEmpty()) {
            voiceChannel.connect().thenAccept(audioConnection -> {
                        AudioSource audio = new LavaplayerAudioSource(event.getApi(), serverMusicManager.player);
                        audioConnection.setAudioSource(audio);
                        audioConnection.setSelfDeafened(true);
                        try {
                            String title = playMusic(mergeArguments(arguments), serverMusicManager, audioPlayerManager).get();
                            textChannel.sendMessage(createSimpleEmbedMessage(title));
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
                String title = playMusic(mergeArguments(arguments), serverMusicManager, audioPlayerManager).get();
                textChannel.sendMessage(createSimpleEmbedMessage(title));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BotException(e);
            } catch (ExecutionException e) {
                throw new BotException(e);
            }
        }

    }

    @Override
    public String getPrefix() {
        return "play";
    }

    @Override
    public String getDescription() {
        return String.format("Plays music from Youtube. Usage: %s%s 'youtube link or title'", properties.getPrefix(), getPrefix());
    }


}
