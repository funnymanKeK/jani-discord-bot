package eu.reitter.discord.janidiscordbot.command.text;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.ITextChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.createSimpleEmbedMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextChannelCommand implements ICommand {

    private final Properties properties;
    private final ITextChannelService textChannelService;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        log.debug("Begin setTextChannel command with arguments {}", Arrays.toString(arguments));
        if (badArguments(event, arguments, 0, true, null)) return;

        TextChannel textChannel = event.getChannel();
        Server server = event.getServer().orElseThrow(() -> new BotException("No server found!"));
        long textChannelId = event.getServerTextChannel().orElseThrow(() -> new BotException("No text channel found!")).getId();
        textChannelService.bindTextChannelToServer(server.getId(), textChannelId);

        String serverName = server.getName();
        String textChannelName = server.getTextChannelById(textChannelId).orElseThrow(() -> new BotException("No textChannel found with id: " + textChannelId)).getName();

        textChannel.sendMessage(createSimpleEmbedMessage(String.format("%s text channel successfully bind to %s server!", textChannelName, serverName)));

        log.debug("SetTextChannel command ended");
    }

    @Override
    public String getPrefix() {
        return "setTextChannel";
    }

    @Override
    public String getDescription() {
        return String.format("Binds the text channel to server. Usage. Usage: %s%s", properties.getPrefix(), getPrefix());
    }
}
