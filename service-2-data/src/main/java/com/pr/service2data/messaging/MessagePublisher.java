package com.pr.service2data.messaging;

import com.pr.service2data.model.ClientDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessagePublisher {
    private final JmsTemplate jmsTemplate;

    @Value("${activemq.queue.send}")
    private String destinationQueue;

    public void sendClientDetails(ClientDetails clientDetails) {
        log.info("Sending client details to queue {}: {}", destinationQueue, clientDetails);
        try {
            jmsTemplate.convertAndSend(destinationQueue, clientDetails);
            log.info("Client details sent successfully");
        } catch (Exception e) {
            log.error("Error sending client details: {}", e.getMessage(), e);
        }
    }
}
