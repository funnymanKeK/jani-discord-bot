package eu.reitter.discord.janidiscordbot.command.music.playlist;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IPlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePlaylistCommand implements ICommand {

    private final Properties properties;
    private final IPlaylistService playlistService;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) throws BotException {
        log.debug("Begin createPlaylist command with arguments {}", Arrays.toString(arguments));

        if (badArguments(event, arguments, 1, true, null)) {
            log.debug("Arguments does not match!");
            return;
        }

        final TextChannel textChannel = event.getChannel();

        String playlistName = arguments[0];
        log.debug("PlaylistName: {}", playlistName);

        try {
            playlistService.create(playlistName, event.getMessageAuthor().getDisplayName());
        } catch (DataIntegrityViolationException e) {
            throw new BotException("Playlist with this name already exists!");
        }

        log.debug("Playlist saved");

        textChannel.sendMessage(createSimpleEmbedMessage(String.format("Playlist '%s' created!", arguments[0])));
        log.debug("CreatePlaylist command ended");
    }

    @Override
    public String getPrefix() {
        return "createPlaylist";
    }

    @Override
    public String getDescription() {
        return String.format("Creates a playlist and you can add songs to it later. Usage: %s%s 'playlist name'", properties.getPrefix(), getPrefix());
    }
}
