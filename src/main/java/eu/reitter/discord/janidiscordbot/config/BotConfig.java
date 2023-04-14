package eu.reitter.discord.janidiscordbot.config;

import eu.reitter.discord.janidiscordbot.listener.MessageListener;
import eu.reitter.discord.janidiscordbot.listener.WelcomeOnVoiceChannelListener;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Configuration
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
public class BotConfig {

    @Autowired
    private Properties properties;
    @Autowired
    private MessageListener messageListener;
    @Autowired(required = false)
    private WelcomeOnVoiceChannelListener welcomeOnVoiceChannelListener;

    @Bean
    DiscordApi discordApi() {
        log.info("Starting bot...");

        DiscordApi api = new DiscordApiBuilder()
                .setToken(properties.getToken())
                .setAllIntents()
                .login()
                .join();
        api.addMessageCreateListener(messageListener);
        if (welcomeOnVoiceChannelListener != null)
            api.addServerVoiceChannelMemberJoinListener(welcomeOnVoiceChannelListener);
        api.updateActivity(properties.getVersion());
        api.setAutomaticMessageCacheCleanupEnabled(true);
        api.setMessageCacheSize(10, 60 * 5);
        api.setReconnectDelay(attempt -> attempt * 2);

        log.info("Bot is up and running!");
        return api;
    }

}
