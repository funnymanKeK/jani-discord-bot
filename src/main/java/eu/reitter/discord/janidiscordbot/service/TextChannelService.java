package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.repository.TextChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextChannelService implements ITextChannelService {

    private final TextChannelRepository textChannelRepository;

    @Override
    public void bindTextChannelToServer(Long serverId, Long textChannelId) {

    }
}
