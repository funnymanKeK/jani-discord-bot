package eu.reitter.discord.janidiscordbot.command.music.playlist;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.TrackEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IPlaylistService;
import eu.reitter.discord.janidiscordbot.service.ISongService;
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
public class AddSongCommand implements ICommand {

    private final Properties properties;
    private final ISongService songService;
    private final IPlaylistService playlistService;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        log.debug("Begin addSong command with arguments {}", Arrays.toString(arguments));

        if (badArguments(event, arguments, 2, false, null)) {
            log.debug("Arguments does not match!");
            return;
        }

        final TextChannel textChannel = event.getChannel();

        String playlistName = arguments[0];
        log.debug("Playlist name: {}", playlistName);

        String titleOrUrl = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
        log.debug("Title or url: {}", titleOrUrl);

        if (!playlistService.playlistExist(playlistName))
            throw new BotException("Playlist with the given name does not exist!");

        try {
            TrackEntity track = songService.addSongToPlaylist(playlistName, titleOrUrl, event.getMessageAuthor().getDisplayName());
            textChannel.sendMessage(createSimpleEmbedMessage(String.format("%s successfully added to %s playlist!", track.getTitle(), track.getPlaylist().getName())));
        } catch (DataIntegrityViolationException e) {
            throw new BotException("Track already on playlist!");
        }

        log.debug("AddSong command ended");
    }

    @Override
    public String getPrefix() {
        return "addSong";
    }

    @Override
    public String getDescription() {
        return String.format("Add songs to a playlist. Usage: %s%s 'playlist name' 'url|song name'", properties.getPrefix(), getPrefix());
    }
}
