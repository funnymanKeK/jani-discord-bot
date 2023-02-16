package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.exception.RepositoryException;

import java.util.List;

public interface IPlaylistService {

    void create(String playlistName, String username);

    void delete(String playlistName);

    List<PlaylistEntity> findAll();

    boolean playlistExist(String name);

    PlaylistEntity findByName(String name);

}
