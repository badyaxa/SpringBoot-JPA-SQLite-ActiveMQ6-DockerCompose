package com.pr.service3data.service;

import com.pr.service3data.messaging.MessagePublisher;
import com.pr.service3data.model.ClientAddress;
import com.pr.service3data.model.ClientDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ExternalService externalService;
    private final MessagePublisher messageSender;

    public void process(ClientDetails clientDetails) {
        log.info("Processing clientDetails: {}", clientDetails);
        ClientAddress clientAddress = externalService.getClientAddress(clientDetails);
        if (clientAddress == null) {
            log.warn("Client address not found for client ID: {}", clientDetails.clientId());
            return;
        }
        messageSender.sendClientAddress(clientAddress);
    }
}
