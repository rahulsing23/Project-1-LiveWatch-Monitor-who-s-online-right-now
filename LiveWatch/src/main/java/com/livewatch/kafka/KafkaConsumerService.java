package com.livewatch.kafka;

import com.livewatch.model.UserEvent;
import com.livewatch.service.WebSocketBroadcastService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final WebSocketBroadcastService webSocketService;

    public KafkaConsumerService(RedisTemplate<String, Object> redisTemplate, WebSocketBroadcastService webSocketService) {
        this.redisTemplate = redisTemplate;
        this.webSocketService = webSocketService;
    }

    @KafkaListener(topics = "users-activity-events", groupId = "livewatch-group")
    public void consumerEvent(UserEvent event){
        logger.info("Enter inside consumerEvent with event-{}", event.toString());

        System.out.println("Received: " + event);
        String key = "live-user-count";

        Object value = redisTemplate.opsForValue().get("live-user-count");
        Long live_count = value == null ? 0L : Long.parseLong(value.toString());

        logger.info("Initial live_count-{}", live_count);
        if(event.getEventType().equals("USER_JOINED")){
            live_count++;
        }
        else {
            if(live_count > 0){
                live_count--;
            }
            else {
                live_count = 0L;
            }
        }
        logger.info("Latest live_count-{}", live_count);
        redisTemplate.opsForValue().set("live-user-count", live_count);
        this.webSocketService.broadcastLiveCount(live_count);

    }
}
