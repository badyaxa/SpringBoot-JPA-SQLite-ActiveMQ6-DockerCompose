package com.pr.service2data.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record ClientDetails(
        UUID clientId,
        String firstName,
        String lastName,
        String middleName
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1636368369469595473L;
}
