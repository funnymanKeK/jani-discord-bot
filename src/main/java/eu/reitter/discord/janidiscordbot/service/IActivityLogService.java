package eu.reitter.discord.janidiscordbot.service;

import eu.reitter.discord.janidiscordbot.entity.ActivityLogEntity;

public interface IActivityLogService {

    void saveActivity(ActivityLogEntity activityLog);
    void updateFinishedActivity(Long messageId);
    void updateErrorActivity(long messageId, String exception);

}
