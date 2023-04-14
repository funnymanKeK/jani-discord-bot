package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.ActivityLogEntity;
import eu.reitter.discord.janidiscordbot.exception.BotException;
import eu.reitter.discord.janidiscordbot.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActivityLogService implements IActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    @Transactional
    public void saveActivity(ActivityLogEntity activityLog) {
        activityLogRepository.save(activityLog);
    }

    @Override
    @Transactional
    public void updateFinishedActivity(Long messageId) {
        ActivityLogEntity activityLogEntity = activityLogRepository.findByMessageId(messageId).orElseThrow(() -> new BotException("No message found with messageId: " + messageId));
        activityLogEntity.setFinishDate(new Timestamp(System.currentTimeMillis()));
        activityLogRepository.save(activityLogEntity);
    }

    @Override
    @Transactional
    public void updateErrorActivity(long messageId, String exception) {
        ActivityLogEntity activityLogEntity = activityLogRepository.findByMessageId(messageId).orElseThrow(() -> new BotException("No message found with messageId: " + messageId));
        activityLogEntity.setFinishDate(new Timestamp(System.currentTimeMillis()));
        activityLogEntity.setErrorMessage(exception);
        activityLogRepository.save(activityLogEntity);
    }
}
