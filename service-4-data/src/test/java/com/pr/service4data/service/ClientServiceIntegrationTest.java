package com.pr.service4data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.service4data.messaging.MessageConsumer;
import com.pr.service4data.messaging.MessagePublisher;
import com.pr.service4data.model.ClientAddress;
import com.pr.service4data.model.ClientCard;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceIntegrationTest {

    private static final String RECEIVE_QUEUE = "client.receive.queue";
    private static final String SEND_QUEUE = "client.send.queue";
    private static final UUID EXISTING_CLIENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID NON_EXISTING_CLIENT_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");
    private static final String EXPECTED_CARD_NUMBER = "4556 7375 8689 9855";

    @Mock
    private JmsTemplate jmsTemplate;

    @Spy
    private ExternalService externalService;

    private ObjectMapper objectMapper;
    private MessagePublisher messagePublisher;
    private ClientService clientService;
    private MessageConsumer messageConsumer;

    @Captor
    private ArgumentCaptor<ClientCard> clientCardCaptor;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        messagePublisher = new MessagePublisher(jmsTemplate);
        ReflectionTestUtils.setField(messagePublisher, "destinationQueue", SEND_QUEUE);

        clientService = new ClientService(externalService, messagePublisher);

        messageConsumer = new MessageConsumer(clientService, objectMapper);
    }

    @Test
    void shouldProcessExistingClientAndSendCardDetails() throws Exception {
        ClientAddress clientAddress = new ClientAddress(
                EXISTING_CLIENT_ID,
                "John",
                "Doe",
                "Smith",
                "123 Main St"
        );
        String jsonMessage = objectMapper.writeValueAsString(clientAddress);
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(jsonMessage);

        messageConsumer.receiveMessage(textMessage);

        verify(externalService).getClientCardNumber(eq(clientAddress));
        verify(jmsTemplate).convertAndSend(eq(SEND_QUEUE), clientCardCaptor.capture());

        ClientCard capturedCard = clientCardCaptor.getValue();
        assertEquals(EXISTING_CLIENT_ID, capturedCard.clientId());
        assertEquals("John", capturedCard.firstName());
        assertEquals("Doe", capturedCard.lastName());
        assertEquals("Smith", capturedCard.middleName());
        assertEquals("123 Main St", capturedCard.address());
        assertEquals(EXPECTED_CARD_NUMBER, capturedCard.cardNumber());
    }

    @Test
    void shouldNotSendMessageWhenClientDoesNotExist() throws Exception {
        ClientAddress clientAddress = new ClientAddress(
                NON_EXISTING_CLIENT_ID,
                "Jane",
                "Smith",
                "Rebecca",
                "456 Oak Ave"
        );
        String jsonMessage = objectMapper.writeValueAsString(clientAddress);
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(jsonMessage);

        messageConsumer.receiveMessage(textMessage);

        verify(externalService).getClientCardNumber(eq(clientAddress));
        verify(jmsTemplate, never()).convertAndSend(anyString(), any(ClientCard.class));
    }

    @Test
    void shouldHandleExceptionWhenInvalidJsonReceived() throws Exception {
        String invalidJson = "{invalid_json}";
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(invalidJson);

        messageConsumer.receiveMessage(textMessage);

        verify(externalService, never()).getClientCardNumber(any());
        verify(jmsTemplate, never()).convertAndSend(anyString(), any(ClientCard.class));
    }

    @Test
    void shouldHandleExceptionWhenJmsExceptionOccurs() throws Exception {
        ClientAddress clientAddress = new ClientAddress(
                EXISTING_CLIENT_ID,
                "John",
                "Doe",
                "Smith",
                "123 Main St"
        );

        String jsonMessage = objectMapper.writeValueAsString(clientAddress);
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(jsonMessage);

        doThrow(new RuntimeException("JMS Error")).when(jmsTemplate).convertAndSend(eq(SEND_QUEUE), any(ClientCard.class));

        messageConsumer.receiveMessage(textMessage);

        verify(externalService).getClientCardNumber(eq(clientAddress));
        verify(jmsTemplate).convertAndSend(eq(SEND_QUEUE), any(ClientCard.class));
    }

    @Test
    void shouldProcessEntireClientFlowWithRealExternalService() throws Exception {
        ClientAddress clientAddress = new ClientAddress(
                EXISTING_CLIENT_ID,
                "John",
                "Doe",
                "Smith",
                "123 Main St"
        );
        String jsonMessage = objectMapper.writeValueAsString(clientAddress);
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(jsonMessage);

        ExternalService realExternalService = new ExternalService();
        ClientService realClientService = new ClientService(realExternalService, messagePublisher);
        MessageConsumer realMessageConsumer = new MessageConsumer(realClientService, objectMapper);

        realMessageConsumer.receiveMessage(textMessage);

        verify(jmsTemplate).convertAndSend(eq(SEND_QUEUE), clientCardCaptor.capture());

        ClientCard capturedCard = clientCardCaptor.getValue();
        assertEquals(EXISTING_CLIENT_ID, capturedCard.clientId());
        assertEquals(EXPECTED_CARD_NUMBER, capturedCard.cardNumber());
    }
}
