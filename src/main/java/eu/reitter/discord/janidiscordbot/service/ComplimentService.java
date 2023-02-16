package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.config.Properties;
import eu.reitter.discord.janidiscordbot.entity.ComplimentEntity;
import eu.reitter.discord.janidiscordbot.repository.ComplimentRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ComplimentService implements IComplimentService {

    private final Properties properties;

    private final ComplimentRepository complimentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ComplimentEntity> getCompliments(int limit) {
        return complimentRepository.findRandomCompliment(properties.getComplimentLimit());
    }
}
