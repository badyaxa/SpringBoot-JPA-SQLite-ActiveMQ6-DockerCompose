package com.pr.service2data.service;

import com.pr.service2data.model.ClientDetails;
import com.pr.service2data.model.ClientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ExternalService {
    private final Map<UUID, ClientDetails> clientsDatabase = new ConcurrentHashMap<>();

    public ExternalService() {
        addClientToDb(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "Іван", "Петренко", "Олександрович");
        addClientToDb(UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "Марія", "Коваленко", "Андріївна");
        addClientToDb(UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "Олег", "Сидоренко", "Ігорович");
        addClientToDb(UUID.fromString("00000000-0000-0000-0000-000000000004"),
                "Наталія", "Шевченко", "Василівна");
        addClientToDb(UUID.fromString("00000000-0000-0000-0000-000000000005"),
                "Артем", "Мельник", "Богданович");
        addClientToDb(UUID.fromString("00000000-0000-0000-0000-000000000006"),
                "Оксана", "Гнатюк", "Сергіївна");
    }

    private void addClientToDb(UUID id, String firstName, String lastName, String middleName) {
        clientsDatabase.put(id, new ClientDetails(id, firstName, lastName, middleName));
    }

    /**
     * Імітує звернення до зовнішнього API
     */
    public ClientDetails getClientDetails(ClientInfo clientInfo) {
        log.info("Requesting client details from external service for client ID: {}", clientInfo.clientId());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return clientsDatabase.containsKey(clientInfo.clientId())
                ? clientsDatabase.get(clientInfo.clientId())
                : null;
    }
}
