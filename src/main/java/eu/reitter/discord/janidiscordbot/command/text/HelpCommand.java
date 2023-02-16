package eu.reitter.discord.janidiscordbot.command.text;

import eu.reitter.discord.janidiscordbot.command.ICommand;
import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.util.BotUtils;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.util.List;

import static eu.reitter.discord.janidiscordbot.util.BotUtils.randomColor;

@Component
@RequiredArgsConstructor
public class HelpCommand implements ICommand {

    private final List<ICommand> commandList;
    private final Properties properties;

    @Override
    public void run(MessageCreateEvent event, String[] arguments) {
        if(badArguments(event, arguments, 0, true, null)) return;
        TextChannel textChannel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder();
      //embed.setAuthor("Jani bot++", null, new File(properties.getImagesPath() + "avatar.png"));
        embed.setTitle("Available commands and their descriptions");

        for (ICommand command : commandList) {
            embed.addInlineField(command.getPrefix(), command.getDescription());
        }

        embed.setColor(randomColor());

        textChannel.sendMessage(embed);
    }

    @Override
    public String getPrefix() {
        return "help";
    }

    @Override
    public String getDescription() {
        return String.format("Provides details about each available commands. Usage: %s%s", properties.getPrefix(), getPrefix());
    }
}
