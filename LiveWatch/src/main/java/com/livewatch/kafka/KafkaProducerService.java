package com.livewatch.kafka;

import com.livewatch.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean sendUserEvent(UserEvent event, String type){

        logger.info("Enter inside sendUserEvent with event-{}", event.toString());
        logger.info("And type-{}", type);
        event.setEventType(type);
        this.kafkaTemplate.send("users-activity-events", event);
        return true;
    }
}
