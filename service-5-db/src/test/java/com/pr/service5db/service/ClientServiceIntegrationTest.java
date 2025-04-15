package com.pr.service5db.service;

import com.pr.service5db.dto.ClientInfoDTO;
import com.pr.service5db.model.ClientInfoEntity;
import com.pr.service5db.repository.ClientInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceIntegrationTest {

    @Mock
    private ClientInfoRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Captor
    private ArgumentCaptor<ClientInfoEntity> clientCaptor;

    private UUID testClientId;
    private ClientInfoDTO testClientInfoDTO;
    private ClientInfoEntity testClientEntity;

    @BeforeEach
    void setUp() {
        testClientId = UUID.randomUUID();

        testClientInfoDTO = new ClientInfoDTO(
                testClientId,
                "Денис",
                "Масюк",
                "Тимофійович",
                "вул. Шевченка, 163, м. Львів, 79000",
                "5444 1234 5678 9521"
        );

        testClientEntity = new ClientInfoEntity();
        testClientEntity.setClientId(testClientId);
        testClientEntity.setFirstName("Денис");
        testClientEntity.setLastName("Масюк");
        testClientEntity.setMiddleName("Тимофійович");
        testClientEntity.setAddress("вул. Шевченка, 163, м. Львів, 79000");
        testClientEntity.setCardNumber("5444 1234 5678 9521");
    }

    @Test
    void shouldSaveNewClient() {
        when(clientRepository.findByClientId(testClientId)).thenReturn(Optional.empty());
        when(clientRepository.save(any(ClientInfoEntity.class))).thenReturn(testClientEntity);

        clientService.save(testClientInfoDTO);

        verify(clientRepository).findByClientId(testClientId);
        verify(clientRepository).save(clientCaptor.capture());

        ClientInfoEntity savedEntity = clientCaptor.getValue();
        assertNotNull(savedEntity);
        assertEquals(testClientId, savedEntity.getClientId());
        assertEquals("Денис", savedEntity.getFirstName());
        assertEquals("Масюк", savedEntity.getLastName());
        assertEquals("Тимофійович", savedEntity.getMiddleName());
        assertEquals("вул. Шевченка, 163, м. Львів, 79000", savedEntity.getAddress());
        assertEquals("5444 1234 5678 9521", savedEntity.getCardNumber());
    }

    @Test
    void shouldUpdateExistingClient() {
        when(clientRepository.findByClientId(testClientId)).thenReturn(Optional.of(testClientEntity));

        ClientInfoDTO updatedClientDTO = new ClientInfoDTO(
                testClientId,
                "Оксана",
                "Данилюк",
                "Петрівна",
                "вул. Свободи, 23, м. Вінниця, 21000",
                "4320 1234 5678 2040"
        );

        clientService.save(updatedClientDTO);

        verify(clientRepository).findByClientId(testClientId);
        verify(clientRepository).save(clientCaptor.capture());

        ClientInfoEntity updatedEntity = clientCaptor.getValue();
        assertNotNull(updatedEntity);
        assertEquals(testClientId, updatedEntity.getClientId());
        assertEquals("Оксана", updatedEntity.getFirstName());
        assertEquals("Данилюк", updatedEntity.getLastName());
        assertEquals("Петрівна", updatedEntity.getMiddleName());
        assertEquals("вул. Свободи, 23, м. Вінниця, 21000", updatedEntity.getAddress());
        assertEquals("4320 1234 5678 2040", updatedEntity.getCardNumber());
    }

    @Test
    void shouldHandleNullValues() {
        when(clientRepository.findByClientId(testClientId)).thenReturn(Optional.empty());

        ClientInfoDTO nullValuesDTO = new ClientInfoDTO(
                testClientId,
                "Денис",
                "Масюк",
                null,
                null,
                null
        );

        clientService.save(nullValuesDTO);

        verify(clientRepository).save(clientCaptor.capture());

        ClientInfoEntity savedEntity = clientCaptor.getValue();
        assertEquals(testClientId, savedEntity.getClientId());
        assertEquals("Денис", savedEntity.getFirstName());
        assertEquals("Масюк", savedEntity.getLastName());
        assertNull(savedEntity.getMiddleName());
        assertNull(savedEntity.getAddress());
        assertNull(savedEntity.getCardNumber());
    }

    @Test
    void shouldVerifyRepositoryInteractions() {
        when(clientRepository.findByClientId(any(UUID.class))).thenReturn(Optional.empty());
        when(clientRepository.count()).thenReturn(1L);

        clientService.save(testClientInfoDTO);

        verify(clientRepository, times(1)).findByClientId(any(UUID.class));

        verify(clientRepository, times(1)).save(any(ClientInfoEntity.class));

        verify(clientRepository, times(1)).count();

        verifyNoMoreInteractions(clientRepository);
    }
}
