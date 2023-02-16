package eu.reitter.discord.janidiscordbot.listener;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener implements MessageCreateListener {

    private final Properties properties;
    private final List<ICommand> commands;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (isBotMessage(event) || isPrivateMessage(event) || badPrefix(event, properties.getPrefix())) return;

        log.info("Processing message started...");
        final TextChannel textChannel = event.getChannel();

        String message = event.getMessageContent();
        log.debug("Message received: {}", message);

        //Removing the command prefix and splitting the arguments into array
        String[] arguments = message.substring(1).split(" ");

        String commandName = arguments[0];
        log.debug("Command: {}", commandName);

        arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
        log.debug("Command arguments: {}", Arrays.toString(arguments));

        ICommand command = findCommand(commandName);
        if (command == null) {
            textChannel.sendMessage(createSimpleEmbedMessage(String.format("Unrecognised command '%s' found!", commandName)));
            log.warn("Unrecognised command '{}' found!", commandName);
            return;
        }

        try {
            long time = System.currentTimeMillis();
            log.info("Start processing {} command", command.getPrefix());
            command.run(event, arguments);
            log.info("Finished processing {} command in {}ms", command.getPrefix(), System.currentTimeMillis() - time);
        } catch (BotException e) {
            log.error("BotRuntimeException occurred: {}", e.getMessage());
            textChannel.sendMessage(createSimpleEmbedMessage("Error happened: " + e.getMessage()));
        }
    }

    @Nullable
    private ICommand findCommand(String commandName) {
        for (ICommand command : commands) {
            if (command.getPrefix().equalsIgnoreCase(commandName)) return command;
        }
        return null;
    }

}