package com.pr.service5db.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record ClientInfoDTO(
        UUID clientId,
        String firstName,
        String lastName,
        String middleName,
        String address,
        String cardNumber
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -5242600821458857636L;
}
