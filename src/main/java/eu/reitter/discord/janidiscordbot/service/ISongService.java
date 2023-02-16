package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.TrackEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;

public interface ISongService {

    TrackEntity addSongToPlaylist(String playlistName, String title, String userName);

}
