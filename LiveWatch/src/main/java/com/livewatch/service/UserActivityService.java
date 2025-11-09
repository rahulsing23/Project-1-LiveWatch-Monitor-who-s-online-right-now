package com.livewatch.service;

import com.livewatch.kafka.KafkaProducerService;
import com.livewatch.model.UserEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class UserActivityService {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityService.class);

    private final KafkaProducerService kafkaProducerService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Async("threadExecutor")
    public void handleUserJoin(UserEvent event) {
       logger.info("inside handleUserJoin called...");
       kafkaProducerService.sendUserEvent(event, "USER_JOINED");
    }

    @Async("threadExecutor")
    public void handleUserLeave(UserEvent event) {
        logger.info("inside handleUserLeave called...");
        kafkaProducerService.sendUserEvent(event, "USER_LEFT");
    }

    @Async("threadExecutor")
    public CompletableFuture<Long> getLiveUserCount(){
        String key = "live-user-count";
        Long live_count = (Long) redisTemplate.opsForValue().get(key);
        return CompletableFuture.completedFuture(live_count);

    }
}
