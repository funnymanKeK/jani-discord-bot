package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.TextChannelEntity;
import eu.reitter.discord.janidiscordbot.repository.TextChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextChannelService implements ITextChannelService {

    private final TextChannelRepository textChannelRepository;

    @Override
    @Transactional
    public void bindTextChannelToServer(Long serverId, Long textChannelId) {
        Optional<TextChannelEntity> textChannelEntityOptional = textChannelRepository.findByTextChannelId(textChannelId);

        TextChannelEntity textChannelEntity;
        if (textChannelEntityOptional.isEmpty()) {
            textChannelEntity = new TextChannelEntity();
            textChannelEntity.setServerId(serverId);
            textChannelEntity.setTextChannelId(textChannelId);
        } else {
            textChannelEntity = textChannelEntityOptional.get();
            textChannelEntity.setTextChannelId(textChannelId);
        }
        textChannelRepository.save(textChannelEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TextChannelEntity> getTextChannelByServerId(Long serverId) {
        return textChannelRepository.findByServerId(serverId);
    }
}
