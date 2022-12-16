package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.exception.RepositoryException;
import eu.reitter.discord.janidiscordbot.repository.PlaylistRepository;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.exception.BotRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaylistService implements IPlaylistService {

    private final PlaylistRepository playlistDAO;

    @Override
    @Transactional
    public void create(String playlistName) {
        PlaylistEntity playlistDTO = new PlaylistEntity();
        playlistDTO.setName(playlistName);
        playlistDAO.save(playlistDTO);
    }

    @Override
    @Transactional
    public void delete(String playlistName) {
        PlaylistEntity playlistDTO = playlistDAO.findByName(playlistName).orElseThrow(() -> new RepositoryException("No playlist found with name: " + playlistName));
        playlistDAO.delete(playlistDTO);
    }

}
