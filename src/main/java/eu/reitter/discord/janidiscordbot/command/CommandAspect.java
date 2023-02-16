package eu.reitter.discord.janidiscordbot.command;

import eu.reitter.discord.janidiscordbot.entity.ActivityLogEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.service.IActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommandAspect {

    private final IActivityLogService activityLogService;

    @Before(value = "execution(* eu.reitter.discord.janidiscordbot.command.*.run(..))")
    public void commandStarted(JoinPoint joinPoint) {
        log.debug("Creating activity log started");

        MessageCreateEvent event = (MessageCreateEvent) joinPoint.getArgs()[0];
        Server server = event.getServer().orElseThrow(() -> new BotException("No server found at aspect!"));
        String args = String.join(" ", (String[]) joinPoint.getArgs()[1]);
        String commandName = getCommandName(joinPoint);

        ActivityLogEntity activityLogEntity = new ActivityLogEntity();
        activityLogEntity.setCommandName(commandName);
        activityLogEntity.setArguments(args);
        activityLogEntity.setUserId(event.getMessageAuthor().getId());
        activityLogEntity.setUserName(event.getMessageAuthor().getDisplayName());
        activityLogEntity.setMessageId(event.getMessageId());
        activityLogEntity.setServerId(server.getId());
        activityLogEntity.setServerName(server.getName());
        activityLogEntity.setStartDate(new Timestamp(new Date().getTime()));
        log.debug("About to save: {}", activityLogEntity);

        activityLogService.saveActivity(activityLogEntity);
        log.debug("Activity log saved");
    }

    @AfterThrowing(pointcut = "execution(* eu.reitter.discord.janidiscordbot.command.*.run(..))", throwing = "ex")
    public void commandException(JoinPoint joinPoint, Exception ex) {
        log.debug("Updating activity log with exception");

        MessageCreateEvent event = (MessageCreateEvent) joinPoint.getArgs()[0];
        long messageId = event.getMessageId();
        String errorMessage = ex.getMessage();

        activityLogService.updateErrorActivity(messageId, errorMessage);
        log.debug("Updating activity log with exception finished");
    }

    @After(value = "execution(* eu.reitter.discord.janidiscordbot.command.*.run(..))")
    public void commandFinished(JoinPoint joinPoint) {
        log.debug("Updating activity log");

        MessageCreateEvent event = (MessageCreateEvent) joinPoint.getArgs()[0];
        long messageId = event.getMessageId();

        activityLogService.updateFinishedActivity(messageId);
        log.debug("Updating activity log finished");
    }

    private String getCommandName(JoinPoint joinPoint) {
        String[] array = joinPoint.toString().split("\\.");
        return array[array.length -2];
    }

}
