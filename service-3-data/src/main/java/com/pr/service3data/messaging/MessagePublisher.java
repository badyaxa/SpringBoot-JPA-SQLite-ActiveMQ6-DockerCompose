package com.pr.service3data.messaging;

import com.pr.service3data.model.ClientAddress;
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

    public void sendClientAddress(ClientAddress clientAddress) {
        log.info("Sending client details with address to queue {}: {}", destinationQueue, clientAddress);
        try {
            jmsTemplate.convertAndSend(destinationQueue, clientAddress);
            log.info("Client details sent successfully");
        } catch (Exception e) {
            log.error("Error sending client details: {}", e.getMessage(), e);
        }
    }
}
