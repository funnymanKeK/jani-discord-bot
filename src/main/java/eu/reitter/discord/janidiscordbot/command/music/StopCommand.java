package eu.reitter.discord.janidiscordbot.command.music;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;
import static eu.reitter.discord.janidiscordbot.util.BotUtils.isAuthorOnVoiceChannel;

@RequiredArgsConstructor
@Component
public class StopCommand implements ICommand {

    private final Properties properties;
    private final AudioManager audioManager;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) throws BotException {
        if (badArguments(event, arguments, 0, true, null)) return;
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
        if (server.getAudioConnection().isEmpty()) {
            textChannel.sendMessage(createSimpleEmbedMessage("Bot is not connected to voice channel!"));
            return;
        }

        ServerMusicManager serverMusicManager = audioManager.get(server.getId());
        serverMusicManager.player.stopTrack();
        serverMusicManager.scheduler.clearQueue();

        textChannel.sendMessage(createSimpleEmbedMessage("Playlist stopped"));
    }

    @Override
    public String getPrefix() {
        return "stop";
    }


    @Override
    public String getDescription() {
        return String.format("Empties the current playlist and stops the music. Usage: %s%s", properties.getPrefix(), getPrefix());
    }
}
