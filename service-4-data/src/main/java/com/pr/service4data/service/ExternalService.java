package com.pr.service4data.service;

import com.pr.service4data.model.ClientAddress;
import com.pr.service4data.model.ClientCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ExternalService {
    private final Map<UUID, String> clientsDatabase = new ConcurrentHashMap<>();

    public ExternalService() {
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "4556 7375 8689 9855");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "5432 1234 5678 9012");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "5105 1051 0510 5100");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000004"),
                "4000 1234 5678 9010");
    }

    private void addMockInfo(UUID id, String cardNumber) {
        clientsDatabase.put(id, cardNumber);
    }

    public ClientCard getClientCardNumber(ClientAddress clientAddress) {
        log.info("Requesting client card number from external service for client ID: {}", clientAddress.clientId());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return clientsDatabase.containsKey(clientAddress.clientId())
                ? ClientCard.from(clientAddress, clientsDatabase.get(clientAddress.clientId()))
                : null;
    }
}
