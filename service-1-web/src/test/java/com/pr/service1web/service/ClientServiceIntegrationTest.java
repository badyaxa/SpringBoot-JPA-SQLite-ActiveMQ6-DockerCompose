package com.pr.service1web.service;

import com.pr.service1web.model.ClientInfo;
import com.pr.service1web.publisher.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceIntegrationTest {

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private ClientService clientService;

    private UUID clientId;

    @BeforeEach
    public void setUp() {
        clientId = UUID.randomUUID();
    }

    @Test
    public void shouldProcessClientAndSendMessage() {
        ClientInfo expectedClientInfo = new ClientInfo(clientId);
        clientService.process(clientId);
        verify(messageSender, times(1)).send(expectedClientInfo);
    }

    @Test
    public void shouldHandleMultipleClients() {
        UUID clientId1 = UUID.randomUUID();
        UUID clientId2 = UUID.randomUUID();
        ClientInfo expectedClientInfo1 = new ClientInfo(clientId1);
        ClientInfo expectedClientInfo2 = new ClientInfo(clientId2);

        clientService.process(clientId1);
        clientService.process(clientId2);

        verify(messageSender, times(1)).send(expectedClientInfo1);
        verify(messageSender, times(1)).send(expectedClientInfo2);
    }
}
