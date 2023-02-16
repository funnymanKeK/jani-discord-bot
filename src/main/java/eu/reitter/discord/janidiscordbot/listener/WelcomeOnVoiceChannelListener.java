package eu.reitter.discord.janidiscordbot.listener;

import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.ComplimentEntity;
import eu.reitter.discord.janidiscordbot.service.IComplimentService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "jani.discord.bot.welcome.listener", name = "enabled")
@RequiredArgsConstructor
public class WelcomeOnVoiceChannelListener implements ServerVoiceChannelMemberJoinListener {

    private final Properties properties;
    private final IComplimentService complimentService;

    @Override
    public void onServerVoiceChannelMemberJoin(ServerVoiceChannelMemberJoinEvent serverVoiceChannelMemberJoinEvent) {
        final TextChannel textChannel = getTextChannel(serverVoiceChannelMemberJoinEvent);
        final String userName = serverVoiceChannelMemberJoinEvent.getUser().getName();

        if (textChannel == null) {
            log.debug("No text channel found");
            return;
        }

        List<ComplimentEntity> complimentsList = complimentService.getCompliments(properties.getComplimentLimit());
        String compliments = complimentsList.stream().map(ComplimentEntity::getCompliment).collect(Collectors.joining(" "));

        textChannel.sendMessage(createSimpleEmbedMessage(String.format("Üdv %s, te %s", userName, compliments)));
    }

    @Nullable
    private TextChannel getTextChannel(ServerVoiceChannelMemberJoinEvent serverVoiceChannelMemberJoinEvent) {
        final Server server = serverVoiceChannelMemberJoinEvent.getServer();

        //If there is a text channel with General name
        List<ServerTextChannel> textChannels = server.getTextChannelsByNameIgnoreCase(properties.getDefaultTextChannelName());
        if (!textChannels.isEmpty()) return textChannels.get(0);

        textChannels = server.getTextChannels();

        //If there are no text channels then return null
        if (textChannels.isEmpty()) return null;

        //If there are text channels but no with the name General then return the first one
        return textChannels.get(0);

    }
}
