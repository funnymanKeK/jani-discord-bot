package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.TextChannelEntity;

import java.util.Optional;

public interface ITextChannelService {

    void bindTextChannelToServer(Long serverId, Long textChannelId);
    Optional<TextChannelEntity> getTextChannelByServerId(Long serverId);

}
