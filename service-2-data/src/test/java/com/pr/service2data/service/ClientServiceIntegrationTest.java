package com.pr.service2data.service;

import com.pr.service2data.messaging.MessagePublisher;
import com.pr.service2data.model.ClientDetails;
import com.pr.service2data.model.ClientInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceIntegrationTest {
    private final UUID existingClientId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID nonExistingClientId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private ExternalService externalService;

    @Mock
    private MessagePublisher messagePublisher;

    @InjectMocks
    private ClientService clientService;

    @Captor
    private ArgumentCaptor<ClientDetails> clientDetailsCaptor;

    private ClientInfo existingClientInfo;
    private ClientInfo nonExistingClientInfo;
    private ClientDetails existingClientDetails;

    @BeforeEach
    public void setUp() {
        existingClientInfo = new ClientInfo(existingClientId);
        nonExistingClientInfo = new ClientInfo(nonExistingClientId);
        existingClientDetails = new ClientDetails(existingClientId, "Іван", "Петренко", "Олександрович");
    }

    @Test
    public void shouldProcessExistingClientAndSendDetails() {
        when(externalService.getClientDetails(existingClientInfo)).thenReturn(existingClientDetails);

        clientService.process(existingClientInfo);

        verify(externalService).getClientDetails(existingClientInfo);
        verify(messagePublisher).sendClientDetails(clientDetailsCaptor.capture());

        ClientDetails capturedDetails = clientDetailsCaptor.getValue();
        assertEquals(existingClientId, capturedDetails.clientId());
        assertEquals("Іван", capturedDetails.firstName());
        assertEquals("Петренко", capturedDetails.lastName());
        assertEquals("Олександрович", capturedDetails.middleName());
    }

    @Test
    public void shouldNotSendMessage_whenClientNotFound() {
        when(externalService.getClientDetails(nonExistingClientInfo)).thenReturn(null);

        clientService.process(nonExistingClientInfo);

        verify(externalService).getClientDetails(nonExistingClientInfo);
        verify(messagePublisher, never()).sendClientDetails(any());
    }

    @Test
    public void shouldHandleMultipleClientsProcessingConcurrently() throws Exception {
        int clientCount = 10;
        CountDownLatch latch = new CountDownLatch(clientCount);

        ClientDetails clientDetails1 = new ClientDetails(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "Іван", "Петренко", "Олександрович");

        ClientDetails clientDetails2 = new ClientDetails(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "Марія", "Коваленко", "Андріївна");

        when(externalService.getClientDetails(any())).thenAnswer(invocation -> {
            ClientInfo info = invocation.getArgument(0);
            if (info.clientId().equals(UUID.fromString("00000000-0000-0000-0000-000000000001"))) {
                return clientDetails1;
            } else if (info.clientId().equals(UUID.fromString("00000000-0000-0000-0000-000000000002"))) {
                return clientDetails2;
            }
            return null;
        });

        for (int i = 0; i < clientCount / 2; i++) {
            new Thread(() -> {
                clientService.process(new ClientInfo(UUID.fromString("00000000-0000-0000-0000-000000000001")));
                latch.countDown();
            }).start();

            new Thread(() -> {
                clientService.process(new ClientInfo(UUID.fromString("00000000-0000-0000-0000-000000000002")));
                latch.countDown();
            }).start();
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);

        assertTrue(completed, "All client processing should complete within timeout");
        verify(externalService, times(clientCount)).getClientDetails(any());
        verify(messagePublisher, times(clientCount)).sendClientDetails(any());
    }
}
