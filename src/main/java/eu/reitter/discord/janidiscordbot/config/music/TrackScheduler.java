package eu.reitter.discord.janidiscordbot.config.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {

        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void clearQueue() {
        queue.clear();
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    public void shuffleQueue() {
        List<AudioTrack> audioTrackList = new ArrayList<>();
        do {
            audioTrackList.add(queue.poll());
        } while (!queue.isEmpty());
        Collections.shuffle(audioTrackList);
        queue.addAll(audioTrackList);
    }

    public List<AudioTrack> getQueuedAudioTracks() {
        return new ArrayList<>(queue);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}

