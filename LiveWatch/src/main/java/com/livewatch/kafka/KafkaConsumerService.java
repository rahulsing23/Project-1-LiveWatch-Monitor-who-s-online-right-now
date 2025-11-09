package com.livewatch.kafka;

import com.livewatch.model.UserEvent;
import com.livewatch.service.WebSocketBroadcastService;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final WebSocketBroadcastService webSocketService;

    public KafkaConsumerService(RedisTemplate<String, Object> redisTemplate, WebSocketBroadcastService webSocketService) {
        this.redisTemplate = redisTemplate;
        this.webSocketService = webSocketService;
    }

    @KafkaListener(topics = "users-activity-events", groupId = "livewatch-group")
    public void consumerEvent(UserEvent event){
        System.out.println("Received: " + event);
        String key = "live-user-count";

        Object value = redisTemplate.opsForValue().get("live-user-count");
        Long live_count = value == null ? 0L : Long.parseLong(value.toString());

        if(event.getEventType().equals("USER_JOINED")){
            live_count++;
        }
        else {
            live_count--;
        }

        redisTemplate.opsForValue().set("live-user-count", live_count);
        this.webSocketService.broadcastLiveCount(live_count);

    }
}
