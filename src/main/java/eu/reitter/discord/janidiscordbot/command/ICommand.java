package eu.reitter.discord.janidiscordbot.command;

import eu.reitter.discord.janidiscordbot.util.BotUtils;
import org.javacord.api.event.message.MessageCreateEvent;

public interface ICommand {

    void run(MessageCreateEvent event, String[] arguments);
    String getPrefix();
    int getMinArgumentNumber();
    String getDescription();

    default boolean enoughArgument(MessageCreateEvent event, String[] arguments, String customErrorMessage) {
        if (arguments.length < getMinArgumentNumber()) {
            event.getChannel().sendMessage(BotUtils.createSimpleEmbedMessage(
                    customErrorMessage == null ? String.format("Not enough command arguments! The minimum is: %s", getMinArgumentNumber()) : customErrorMessage
            ));
            return false;
        } else {
            return true;
        }
    }

}