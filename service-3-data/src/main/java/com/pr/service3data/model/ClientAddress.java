package com.pr.service3data.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record ClientAddress(
        UUID clientId,
        String firstName,
        String lastName,
        String middleName,
        String address
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -5242600821458857636L;

    public static ClientAddress from(ClientDetails details, String address) {
        return new ClientAddress(
                details.clientId(),
                details.firstName(),
                details.lastName(),
                details.middleName(),
                address
        );
    }
}
