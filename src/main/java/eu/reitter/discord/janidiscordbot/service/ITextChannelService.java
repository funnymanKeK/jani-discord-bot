package eu.reitter.discord.janidiscordbot.service;

public interface ITextChannelService {

    void bindTextChannelToServer(Long serverId, Long textChannelId);

}
