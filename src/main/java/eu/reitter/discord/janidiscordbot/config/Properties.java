package eu.reitter.discord.janidiscordbot.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "jani.discord.bot")
public class Properties {

    @NotBlank
    private String token;
    @NotBlank
    private String prefix;
    @NotNull
    private Integer insultCommandLimit;
    @NotBlank
    private String version;
    @NotBlank
    private String imagesPath;

}
