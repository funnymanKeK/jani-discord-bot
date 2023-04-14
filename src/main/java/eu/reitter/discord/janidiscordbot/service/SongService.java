package eu.reitter.discord.janidiscordbot.service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import eu.reitter.discord.janidiscordbot.config.music.AudioManager;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.repository.PlaylistRepository;
import eu.reitter.discord.janidiscordbot.repository.SongRepository;
import eu.reitter.discord.janidiscordbot.entity.PlaylistEntity;
import eu.reitter.discord.janidiscordbot.entity.TrackEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.util.BotUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService implements ISongService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final AudioManager audioManager;
    private final AudioPlayerManager audioPlayerManager;

    @Override
    @CacheEvict(value = "playlists", allEntries = true)
    @Transactional
    public TrackEntity addSongToPlaylist(String playlistName, String titleOrUrl, String userName) throws BotException {
        PlaylistEntity playlist = playlistRepository.findByName(playlistName).orElseThrow(() -> new BotException("No playlist found with name: " + playlistName));

        ServerMusicManager serverMusicManager = audioManager.get(0);

        try {
            AudioTrack audioTrack = BotUtils.getAudioTrack(titleOrUrl, serverMusicManager, audioPlayerManager).get();

            TrackEntity trackEntity = new TrackEntity();
            trackEntity.setPlaylist(playlist);
            trackEntity.setTitle(audioTrack.getInfo().title);
            trackEntity.setUrl(audioTrack.getInfo().uri);
            trackEntity.setCreateUser(userName);
            trackEntity.setModifyUser(userName);
            trackEntity.setCreateDate(new Timestamp(System.currentTimeMillis()));
            trackEntity.setModifyDate(new Timestamp(System.currentTimeMillis()));
            playlist.getTracks().add(trackEntity);
            return songRepository.save(trackEntity);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BotException(e);
        } catch (ExecutionException e) {
            throw new BotException(e);
        }


    }
}
