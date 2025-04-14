package com.pr.service4data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientCard(
        UUID clientId,
        String firstName,
        String lastName,
        String middleName,
        String address,
        String carNumber
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -8800158081793746133L;

    public static ClientCard from(ClientAddress details, String carNumber) {
        return new ClientCard(
                details.clientId(),
                details.firstName(),
                details.lastName(),
                details.middleName(),
                details.address(),
                carNumber
        );
    }
}
