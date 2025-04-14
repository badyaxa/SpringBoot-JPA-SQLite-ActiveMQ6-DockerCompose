package com.pr.service1web.service;

import com.pr.service1web.model.ClientInfo;
import com.pr.service1web.publisher.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final MessageSender messageSender;

    public void process(UUID clientId) {
        log.info("Processing clientId: {}", clientId);
        messageSender.send(new ClientInfo(clientId));
    }
}
