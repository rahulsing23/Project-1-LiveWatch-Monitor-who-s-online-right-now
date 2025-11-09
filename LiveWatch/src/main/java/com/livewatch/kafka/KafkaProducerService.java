package com.livewatch.kafka;

import com.livewatch.model.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean sendUserEvent(UserEvent event, String type){
        event.setEventType(type);
        this.kafkaTemplate.send("users-activity-events", event);
        return true;
    }
}
