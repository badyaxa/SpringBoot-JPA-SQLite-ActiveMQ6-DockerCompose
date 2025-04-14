package com.pr.service4data.model;

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
}
