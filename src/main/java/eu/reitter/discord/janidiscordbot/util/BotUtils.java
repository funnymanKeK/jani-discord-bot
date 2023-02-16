package eu.reitter.discord.janidiscordbot.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import eu.reitter.discord.janidiscordbot.config.music.ServerMusicManager;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import org.apache.commons.validator.routines.UrlValidator;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class BotUtils {

    public static boolean isBotMessage(MessageCreateEvent event) {
        return event.getMessageAuthor().isBotUser();
    }

    public static boolean isPrivateMessage(MessageCreateEvent event) {
        return event.getMessage().isPrivateMessage();
    }

    public static boolean badPrefix(MessageCreateEvent event, String prefix) {
        return !event.getMessageContent().startsWith(prefix);
    }

    public static boolean isAuthorOnVoiceChannel(MessageCreateEvent event) {
        return event.getMessageAuthor().getConnectedVoiceChannel().isPresent();
    }

    public static boolean isBotConnected(MessageCreateEvent event, ServerVoiceChannel voiceChannel) {
        return voiceChannel.isConnected(event.getApi().getYourself());
    }

    public static EmbedBuilder createSimpleEmbedMessage(String content) {
        return new EmbedBuilder().setDescription(content).setColor(randomColor());
    }

    public static String mergeArguments(String[] arguments) {
        return String.join(" ", arguments);
    }

    public static Future<String> playMusic(String url, ServerMusicManager serverMusicManager, AudioPlayerManager audioPlayerManager) {
        if (!UrlValidator.getInstance().isValid(url)) url = "ytsearch: " + url;

        CompletableFuture<String> title = new CompletableFuture<>();
        audioPlayerManager.loadItemOrdered(serverMusicManager, url, new FunctionalResultHandler(

                audioTrack -> {
                    serverMusicManager.scheduler.queue(audioTrack);
                    title.complete(audioTrack.getInfo().title);
                },
                audioPlaylist -> {

                    if (audioPlaylist.isSearchResult()) {

                        serverMusicManager.scheduler.queue(audioPlaylist.getTracks().get(0));
                        title.complete(audioPlaylist.getTracks().get(0).getInfo().title);
                    } else {

                        audioPlaylist.getTracks().forEach(audioTrack -> {
                            serverMusicManager.scheduler.queue(audioTrack);
                            title.complete(audioTrack.getInfo().title);
                        });
                    }
                },
                () -> title.complete("We couldn't find the track."),
                BotException::new));

        return title;
    }

    public static Future<AudioTrack> getAudioTrack(String url, ServerMusicManager serverMusicManager, AudioPlayerManager audioPlayerManager) {
        if (!UrlValidator.getInstance().isValid(url)) url = "ytsearch: " + url;

        CompletableFuture<AudioTrack> audioTrackFuture = new CompletableFuture<>();
        audioPlayerManager.loadItemOrdered(serverMusicManager, url, new FunctionalResultHandler(
                audioTrackFuture::complete,
                audioPlaylist -> {
                    if (audioPlaylist.isSearchResult()) {
                        audioTrackFuture.complete(audioPlaylist.getTracks().get(0));
                    } else {
                        throw new BotException("You cannot add playlist to playlist!");
                    }
                },
                () -> {
                    throw new BotException("We couldn't find the track.");
                },
                e -> {
                    throw new BotException(e);
                }));
        return audioTrackFuture;
    }

    public static Color randomColor() {
        Random random = new Random();
        return new Color(
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat()
        );
    }

    private BotUtils() {
        //We do not need constructor for utility class :)
    }
}
