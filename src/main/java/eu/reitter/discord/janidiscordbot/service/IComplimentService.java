package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.ComplimentEntity;

import java.util.List;

public interface IComplimentService {

    List<ComplimentEntity> getCompliments(int limit);

}
