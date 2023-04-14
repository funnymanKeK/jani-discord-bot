package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.exception.RepositoryException;
import eu.reitter.discord.janidiscordbot.repository.PlaylistRepository;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService implements IPlaylistService {

    private final PlaylistRepository playlistRepository;

    @Override
    @CacheEvict(value = "playlists", allEntries = true)
    @Transactional
    public void create(String playlistName, String username) throws BotException {
        log.info("Creating new playlist with name: {}, username: {}", playlistName, username);
        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setName(playlistName);
        playlist.setCreateUser(username);
        playlist.setModifyUser(username);
        playlist.setCreateDate(new Timestamp(System.currentTimeMillis()));
        playlist.setModifyDate(new Timestamp(System.currentTimeMillis()));
        playlistRepository.save(playlist);
        log.info("Finished creating playlist");
    }

    @Override
    @CacheEvict(value = "playlists", allEntries = true)
    @Transactional
    public void delete(String playlistName) throws RepositoryException {
        PlaylistEntity playlistDTO = playlistRepository.findByName(playlistName).orElseThrow(() -> new RepositoryException("No playlist found with name: " + playlistName));
        playlistRepository.delete(playlistDTO);
    }

    @Override
    @Cacheable("playlists")
    @Transactional(readOnly = true)
    public List<PlaylistEntity> findAll() {
        return playlistRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean playlistExist(String name) {
        return playlistRepository.countByName(name) == 1;
    }

    @Override
    @Transactional(readOnly = true)
    public PlaylistEntity findByName(String username) {
        return playlistRepository.findByName(username).orElseThrow(() -> new BotException("Playlist with the given name does not exist!"));
    }

}
