package com.pr.service1web.publisher;

import com.pr.service1web.model.ClientInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {
    private final JmsTemplate jmsTemplate;

    @Value("${activemq.queue.send}")
    private String destinationQueue;

    public void send(ClientInfo clientInfo) {
        log.info("Sending client details to queue {}: {}", destinationQueue, clientInfo);

        try {
            jmsTemplate.convertAndSend(destinationQueue, clientInfo);
            log.info("Client details sent successfully");
        } catch (JmsException e) {
            log.error("Error processing/sending a message: {}", e.getMessage(), e);
        }
    }
}
