package eu.reitter.discord.janidiscordbot.exception;

public class BotRuntimeException extends RuntimeException {

    public BotRuntimeException() {
    }

    public BotRuntimeException(Throwable cause) {
        super(cause);
    }

    public BotRuntimeException(String message) {
        super(message);
    }
}
