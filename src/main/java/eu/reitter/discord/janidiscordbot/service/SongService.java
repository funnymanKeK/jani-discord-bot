package eu.reitter.discord.janidiscordbot.service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.repository.PlaylistRepository;
import eu.reitter.discord.janidiscordbot.repository.SongRepository;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.entity.TrackEntity;
import eu.reitter.discord.janidiscordbot.exception.BotRuntimeException;
import eu.reitter.discord.janidiscordbot.util.BotUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class SongService implements ISongService {

    private final PlaylistRepository playlistDAO;
    private final SongRepository songDAO;
    private final AudioManager audioManager;
    private final AudioPlayerManager audioPlayerManager;

    @Override
    @Transactional
    public void addSongToPlaylist(String playlistName, String titleOrUrl) {
        PlaylistEntity playlistDTO = playlistDAO.findByName(playlistName).orElseThrow(() -> new BotRuntimeException("No playlist found with name: " + playlistName));

        ServerMusicManager serverMusicManager = audioManager.get(1);

        try {
            AudioTrack audioTrack = BotUtils.getAudioTrack(titleOrUrl, serverMusicManager, audioPlayerManager).get();

            TrackEntity songDTO = new TrackEntity();
            songDTO.setPlaylistDTO(playlistDTO);
            songDTO.setTitle(audioTrack.getInfo().title);
            song
            songDAO.save(songDTO);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BotRuntimeException(e);
        } catch (ExecutionException e) {
            throw new BotRuntimeException(e);
        }


    }
}
