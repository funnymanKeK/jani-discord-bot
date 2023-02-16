package eu.reitter.discord.janidiscordbot.exception;

public class BotException extends RuntimeException {

    public BotException() {
    }

    public BotException(Throwable cause) {
        super(cause);
    }

    public BotException(String message) {
        super(message);
    }
}
