package com.pr.service4data.messaging;

import com.pr.service4data.model.ClientCard;
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

    public void sendClientCardNumber(ClientCard clientCard) {
        log.info("Sending client details with card number to queue {}: {}", destinationQueue, clientCard);
        try {
            jmsTemplate.convertAndSend(destinationQueue, clientCard);
            log.info("Client details sent successfully");
        } catch (Exception e) {
            log.error("Error sending client details: {}", e.getMessage(), e);
        }
    }
}
