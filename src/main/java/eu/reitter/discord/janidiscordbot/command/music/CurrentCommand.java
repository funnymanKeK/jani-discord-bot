package eu.reitter.discord.janidiscordbot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.exception.BotRuntimeException;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;
import static eu.reitter.discord.janidiscordbot.util.BotUtils.isAuthorOnVoiceChannel;

@RequiredArgsConstructor
@Component
public class CurrentCommand implements ICommand {

    private final Properties properties;
    private final AudioManager audioManager;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        final TextChannel textChannel = event.getChannel();

        if (!isAuthorOnVoiceChannel(event)) {
            textChannel.sendMessage(createSimpleEmbedMessage("You are not connected to any voice channels!"));
            return;
        }

        final ServerVoiceChannel voiceChannel = event.getMessageAuthor().getConnectedVoiceChannel().orElseThrow(() -> new BotRuntimeException("No voice channel found!"));

        if (!voiceChannel.canYouSee() || !voiceChannel.canYouConnect()) {
            textChannel.sendMessage(createSimpleEmbedMessage("The bot cannot see or connect to the voice channel!"));
            return;
        }

        if (!voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK)) {
            textChannel.sendMessage(createSimpleEmbedMessage("The bot does not have permission to speak!"));
            return;
        }

        final Server server = event.getServer().orElseThrow(() -> new BotRuntimeException("No server found!"));
        if (server.getAudioConnection().isEmpty()) {
            textChannel.sendMessage(createSimpleEmbedMessage("Bot is not connected to voice channel!"));
            return;
        }

        ServerMusicManager serverMusicManager = audioManager.get(server.getId());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Jani bot++", null, new File(properties.getImagesPath() + "avatar.png"));
        embed.setTitle("Songs on the playlist in order:");

        int prior = 1;
        for (AudioTrack audioTrack : serverMusicManager.scheduler.getQueuedAudioTracks()) {
            embed.addField(prior + " - " + audioTrack.getInfo().author, audioTrack.getInfo().title);
            prior++;
        }

        embed.setColor(Color.MAGENTA);
        textChannel.sendMessage(embed);
    }

    @Override
    public String getPrefix() {
        return "current";
    }

    @Override
    public int getMinArgumentNumber() {
        return 0;
    }

    @Override
    public String getDescription() {
        return String.format("Prints the current songs in the playlist. Usage: %s%s", properties.getPrefix(), getPrefix());
    }
}
