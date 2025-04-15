package com.pr.service5db.service;

import com.pr.service5db.dto.ClientInfoDTO;
import com.pr.service5db.model.ClientInfoEntity;
import com.pr.service5db.repository.ClientInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientInfoRepository clientRepository;

    @Transactional
    public void save(ClientInfoDTO clientInfoDTO) {
        log.debug("Saving client info: {}", clientInfoDTO);

        Optional<ClientInfoEntity> existingClient = clientRepository.findByClientId(clientInfoDTO.clientId());

        if (existingClient.isPresent()) {
            ClientInfoEntity client = existingClient.get();
            updateClientInfo(client, clientInfoDTO);
            clientRepository.save(client);
            log.info("Updated existing client with ID: {}", client.getClientId());
        } else {
            ClientInfoEntity newClient = mapToEntity(clientInfoDTO);
            clientRepository.save(newClient);
            log.info("Saved new client with ID: {}", newClient.getClientId());
        }
        log.info("clientRepository.count() = " + clientRepository.count());
    }

    private void updateClientInfo(ClientInfoEntity client, ClientInfoDTO dto) {
        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setMiddleName(dto.middleName());
        client.setAddress(dto.address());
        client.setCardNumber(dto.cardNumber());
    }

    private ClientInfoEntity mapToEntity(ClientInfoDTO dto) {
        return new ClientInfoEntity(
                dto.clientId(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.address(),
                dto.cardNumber()
        );
    }
}
