package com.pr.service3data.service;

import com.pr.service3data.messaging.MessagePublisher;
import com.pr.service3data.model.ClientAddress;
import com.pr.service3data.model.ClientDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceIntegrationTest {

    private final UUID clientId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final String address = "вул. Незалежності, 30, м. Тернопіль, 46000";
    @Mock
    private ExternalService externalService;
    @Mock
    private MessagePublisher messagePublisher;
    @InjectMocks
    private ClientService clientService;
    @Captor
    private ArgumentCaptor<ClientAddress> clientAddressCaptor;
    private ClientDetails testClientDetails;
    private ClientAddress testClientAddress;

    @BeforeEach
    public void setUp() {
        testClientDetails = new ClientDetails(
                clientId,
                "Іван",
                "Петренко",
                "Васильович"
        );

        testClientAddress = new ClientAddress(
                clientId,
                "Іван",
                "Петренко",
                "Васильович",
                address
        );
    }

    @Test
    public void shouldProcessClient_whenClientAddressExists() {
        when(externalService.getClientAddress(testClientDetails)).thenReturn(testClientAddress);

        clientService.process(testClientDetails);

        verify(externalService).getClientAddress(testClientDetails);
        verify(messagePublisher).sendClientAddress(clientAddressCaptor.capture());

        ClientAddress capturedAddress = clientAddressCaptor.getValue();
        assertEquals(clientId, capturedAddress.clientId());
        assertEquals("Іван", capturedAddress.firstName());
        assertEquals("Петренко", capturedAddress.lastName());
        assertEquals("Васильович", capturedAddress.middleName());
        assertEquals(address, capturedAddress.address());
    }

    @Test
    public void shouldNotSendMessage_whenClientAddressNotFound() {
        when(externalService.getClientAddress(testClientDetails)).thenReturn(null);

        clientService.process(testClientDetails);

        verify(externalService).getClientAddress(testClientDetails);
        verifyNoInteractions(messagePublisher);
    }
}
