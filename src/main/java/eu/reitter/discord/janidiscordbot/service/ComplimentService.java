package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.ComplimentEntity;
import eu.reitter.discord.janidiscordbot.repository.ComplimentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ComplimentService implements IComplimentService {

    private final ComplimentRepository complimentRepository;
    private final Properties properties;

    @Override
    @Transactional(readOnly = true)
    public List<ComplimentEntity> getDirtyWords(int limit) {
        return complimentRepository.findRandomCompliment(properties.getInsultCommandLimit());
    }
}
