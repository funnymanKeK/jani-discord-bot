package eu.reitter.discord.janidiscordbot.command;

import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.util.BotUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;

public interface ICommand {

    void run(MessageCreateEvent event, String[] arguments);

    String getPrefix();

    String getDescription();

    default boolean badArguments(MessageCreateEvent event, String[] arguments, int minArguments, boolean exactNumber, String customErrorMessage) {
        TextChannel textChannel = event.getChannel();
        String minArgumentsText = exactNumber ? "It must be" : "The minimum is";
        String errorMessage = customErrorMessage == null ? String.format("Not enough command arguments! %s %s",minArgumentsText, minArguments) : customErrorMessage;
        if (exactNumber) {
            if (arguments.length != minArguments) {
                textChannel.sendMessage(BotUtils.createSimpleEmbedMessage(errorMessage));
                return true;
            } else {
                return false;
            }
        } else {
            if (arguments.length < minArguments) {
                textChannel.sendMessage(BotUtils.createSimpleEmbedMessage(errorMessage));
                return true;
            } else {
                return false;
            }
        }
    }
}