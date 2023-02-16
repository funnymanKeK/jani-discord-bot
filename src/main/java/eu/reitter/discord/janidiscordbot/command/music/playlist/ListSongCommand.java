package eu.reitter.discord.janidiscordbot.command.music.playlist;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.entity.TrackEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IPlaylistService;
import eu.reitter.discord.janidiscordbot.service.ISongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.randomColor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListSongCommand implements ICommand {

    private final Properties properties;
    private final IPlaylistService playlistService;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        log.debug("Begin addSong command with arguments {}", Arrays.toString(arguments));

        if (badArguments(event, arguments, 1, true, null)) {
            log.debug("Arguments does not match!");
            return;
        }
        final TextChannel textChannel = event.getChannel();

        String playlistName = arguments[0];
        log.debug("Playlist name: {}", playlistName);

        PlaylistEntity playlist = playlistService.findByName(playlistName);
        if(playlist.getTracks().isEmpty()) throw new BotException("Playlist does not contain any tracks!");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Songs of %s playlist...", playlistName));

        for(int i = 0; i < playlist.getTracks().size(); i++) {
            embedBuilder.addField(i + ".", playlist.getTracks().get(i).getTitle());
        }

        embedBuilder.setColor(randomColor());

        textChannel.sendMessage(embedBuilder);
        log.debug("ListSong command ended");
    }

    @Override
    public String getPrefix() {
        return "listSong";
    }

    @Override
    public String getDescription() {
        return String.format("Lists the tracks of the given playlist. Usage: %s%s 'playlist name'", properties.getPrefix(), getPrefix());
    }
}
