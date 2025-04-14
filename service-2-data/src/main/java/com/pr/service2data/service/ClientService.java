package com.pr.service2data.service;

import com.pr.service2data.messaging.MessagePublisher;
import com.pr.service2data.model.ClientDetails;
import com.pr.service2data.model.ClientInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ExternalService externalService;
    private final MessagePublisher messageSender;

    public void process(ClientInfo clientInfo) {
        log.info("Processing clientInfo: {}", clientInfo);
        ClientDetails clientDetails = externalService.getClientDetails(clientInfo);
        if (clientDetails == null) {
            log.warn("Client details not found for client ID: {}", clientInfo.clientId());
            return;
        }
        messageSender.sendClientDetails(clientDetails);
    }
}
