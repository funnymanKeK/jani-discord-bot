package eu.reitter.discord.janidiscordbot.command.music.playlist;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IPlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.randomColor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListPlaylistCommand implements ICommand {

    private final Properties properties;
    private final IPlaylistService playlistService;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        log.debug("Begin listPlaylist command with arguments {}", Arrays.toString(arguments));

        if (badArguments(event, arguments, 0, true, null)) {
            log.debug("Arguments does not match!");
            return;
        }

        final TextChannel textChannel = event.getChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Available playlists");

        List<PlaylistEntity> playlists = playlistService.findAll();
        if (playlists.isEmpty()) throw new BotException("There are no playlists :(");

        for (PlaylistEntity playlist : playlists) {
            String detail = String.format("Author: %s %n Created: %s %n Song count: %s", playlist.getCreateUser(), playlist.getCreateDate(), playlist.getTracks().size());
            embedBuilder.addInlineField(playlist.getName(), detail);
        }

        embedBuilder.setColor(randomColor());

        textChannel.sendMessage(embedBuilder);
        log.debug("ListPlaylist command ended");
    }

    @Override
    public String getPrefix() {
        return "listPlaylist";
    }

    @Override
    public String getDescription() {
        return String.format("Lists the available playlists. Usage: %s%s", properties.getPrefix(), getPrefix());
    }
}
