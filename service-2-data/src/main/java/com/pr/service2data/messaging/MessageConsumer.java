package com.pr.service2data.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.service2data.model.ClientInfo;
import com.pr.service2data.service.ClientService;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {
    private final ClientService clientService;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${activemq.queue.receive}")
    public void receiveMessage(TextMessage textMessage) {
        log.info("Received message from queue: {}", textMessage);
        try {
            clientService.process(objectMapper.readValue(textMessage.getText(), ClientInfo.class));
        } catch (Exception e) {
            log.error("Error processing client message: {}", e.getMessage(), e);
        }
    }
}
