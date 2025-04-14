package com.pr.service4data.service;

import com.pr.service4data.messaging.MessagePublisher;
import com.pr.service4data.model.ClientAddress;
import com.pr.service4data.model.ClientCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ExternalService externalService;
    private final MessagePublisher messageSender;

    public void process(ClientAddress clientAddress) {
        log.info("Processing clientDetails: {}", clientAddress);
        ClientCard clientCardNumber = externalService.getClientCardNumber(clientAddress);
        if (clientCardNumber == null) {
            log.warn("Client card number not found for client ID: {}", clientAddress.clientId());
            return;
        }
        messageSender.sendClientCardNumber(clientCardNumber);
    }
}
