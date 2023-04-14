
package eu.reitter.discord.janidiscordbot.listener;

import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.ComplimentEntity;
import eu.reitter.discord.janidiscordbot.entity.TextChannelEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IComplimentService;
import eu.reitter.discord.janidiscordbot.service.ITextChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "jani.discord.bot.welcome.listener", name = "enabled")
@RequiredArgsConstructor
public class WelcomeOnVoiceChannelListener implements ServerVoiceChannelMemberJoinListener {

    private final Properties properties;
    private final IComplimentService complimentService;
    private final ITextChannelService textChannelService;

    @Override
    public void onServerVoiceChannelMemberJoin(ServerVoiceChannelMemberJoinEvent serverVoiceChannelMemberJoinEvent) {
        if(serverVoiceChannelMemberJoinEvent.getUser().isBot()) return;

        final Server server = serverVoiceChannelMemberJoinEvent.getServer();
        Long serverId = server.getId();
        Optional<TextChannelEntity> textChannelEntityOptional = textChannelService.getTextChannelByServerId(serverId);
        if (textChannelEntityOptional.isEmpty())
            throw new BotException(String.format("Pls bind a text channel to server with command: %ssetTextChannel", properties.getPrefix()));

        final TextChannel textChannel = server.getTextChannelById(textChannelEntityOptional.get().getTextChannelId()).orElseThrow(() -> new BotException("No text channel found with the given id!"));
        final String userName = serverVoiceChannelMemberJoinEvent.getUser().getName();

        if (textChannel == null) {
            log.debug("No text channel found");
            return;
        }

        List<ComplimentEntity> complimentsList = complimentService.getCompliments(properties.getComplimentLimit());
        String compliments = complimentsList.stream()
                .map(ComplimentEntity::getCompliment)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));

        textChannel.sendMessage(createSimpleEmbedMessage(String.format("Üdv %s, te %s", userName, compliments)));
    }

}

