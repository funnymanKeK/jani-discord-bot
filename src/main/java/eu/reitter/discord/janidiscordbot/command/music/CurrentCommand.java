package eu.reitter.discord.janidiscordbot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentCommand implements ICommand {

    private final Properties properties;
    private final AudioManager audioManager;

    private static final String AVATAR_FILENAME = "avatar.png";

    @Override
    public void run(MessageCreateEvent event, String[] arguments) throws BotException {
        log.debug("Begin current command with arguments {}", Arrays.toString(arguments));

        if (badArguments(event, arguments, 0, true, null)) {
            log.debug("Arguments does not match!");
            return;
        }

        final TextChannel textChannel = event.getChannel();

        if (!isAuthorOnVoiceChannel(event)) {
            textChannel.sendMessage(createSimpleEmbedMessage("You are not connected to any voice channels!"));
            log.debug("User is not connected to voice channel");
            return;
        }

        final ServerVoiceChannel voiceChannel = event.getMessageAuthor().getConnectedVoiceChannel().orElseThrow(() -> new BotException("No voice channel found!"));

        if (!voiceChannel.canYouSee() || !voiceChannel.canYouConnect()) {
            textChannel.sendMessage(createSimpleEmbedMessage("The bot cannot see or connect to the voice channel!"));
            log.debug("User and bot are not on the same voice channel");
            return;
        }

        if (!voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK)) {
            textChannel.sendMessage(createSimpleEmbedMessage("The bot does not have permission to speak!"));
            log.debug("Bot does not have speak permission");
            return;
        }

        final Server server = event.getServer().orElseThrow(() -> new BotException("No server found!"));
        if (server.getAudioConnection().isEmpty()) {
            textChannel.sendMessage(createSimpleEmbedMessage("Bot is not connected to voice channel!"));
            log.debug("Bot is not connected to voice channel");
            return;
        }

        ServerMusicManager serverMusicManager = audioManager.get(server.getId());

        EmbedBuilder embed = new EmbedBuilder();
  //      embed.setAuthor("Jani bot++", null, new File(properties.getImagesPath() + AVATAR_FILENAME));
        embed.setTitle("Songs on the playlist in order:");

        int prior = 1;
        for (AudioTrack audioTrack : serverMusicManager.scheduler.getQueuedAudioTracks()) {
            embed.addField(prior + " - " + audioTrack.getInfo().author, audioTrack.getInfo().title);
            prior++;
        }

        embed.setColor(randomColor());
        textChannel.sendMessage(embed);
        log.debug("Current command ended");
    }

    @Override
    public String getPrefix() {
        return "current";
    }

    @Override
    public String getDescription() {
        return String.format("Prints the current songs in the playlist. Usage: %s%s", properties.getPrefix(), getPrefix());
    }
}
