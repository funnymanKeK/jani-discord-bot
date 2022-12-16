package eu.reitter.discord.janidiscordbot.command.music;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.service.IPlaylistService;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;


@RequiredArgsConstructor
@Component
public class CreatePlaylistCommand implements ICommand {

    private final Properties properties;
    private final IPlaylistService playlistService;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        final TextChannel textChannel = event.getChannel();

        if (!enoughArgument(event, arguments, null)) return;

        playlistService.create(arguments[0]);

        textChannel.sendMessage(createSimpleEmbedMessage(String.format("Playlist '%s' created!", arguments[0])));
    }

    @Override
    public String getPrefix() {
        return "createPlaylist";
    }

    @Override
    public int getMinArgumentNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return String.format("Creates a playlist and you can add songs to it later. Usage: %s%s 'playlist name'", properties.getPrefix(), getPrefix());
    }
}
