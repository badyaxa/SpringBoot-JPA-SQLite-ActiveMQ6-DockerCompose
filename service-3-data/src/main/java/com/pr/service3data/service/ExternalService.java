package com.pr.service3data.service;

import com.pr.service3data.model.ClientAddress;
import com.pr.service3data.model.ClientDetails;
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
                "вул. Незалежності, 30, м. Тернопіль, 46000");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "вул. Соборна, 3, м. Вінниця, 21000");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "вул. Хрещатик, 22, м. Київ, 01001");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000004"),
                "просп. Свободи, 16, м. Львів, 79000");
        addMockInfo(UUID.fromString("00000000-0000-0000-0000-000000000005"),
                "вул. Сумська, 45, м. Харків, 61000");
    }

    private void addMockInfo(UUID id, String address) {
        clientsDatabase.put(id, address);
    }

    public ClientAddress getClientAddress(ClientDetails clientDetails) {
        log.info("Requesting client details from external service for client ID: {}", clientDetails.clientId());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return clientsDatabase.containsKey(clientDetails.clientId())
                ? ClientAddress.from(clientDetails, clientsDatabase.get(clientDetails.clientId()))
                : null;
    }
}
