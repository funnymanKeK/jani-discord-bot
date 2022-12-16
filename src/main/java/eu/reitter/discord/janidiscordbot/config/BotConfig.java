package eu.reitter.discord.janidiscordbot.config;

import eu.reitter.discord.janidiscordbot.listener.MessageListener;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties
public class BotConfig {

    private final Properties properties;
    private final MessageListener messageListener;

    @Bean
    DiscordApi discordApi() {
        log.info("Starting bot...");

        DiscordApi api = new DiscordApiBuilder()
                .setToken(properties.getToken())
                .setAllIntents()
                .login()
                .join();
        api.addMessageCreateListener(messageListener);
        api.updateActivity(properties.getVersion());
        api.setAutomaticMessageCacheCleanupEnabled(true);
        api.setMessageCacheSize(10, 60 * 5);
        api.setReconnectDelay(attempt -> attempt * 2);

        log.info("Bot is up and running!");
        return api;
    }

}
