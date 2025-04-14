package com.pr.service3data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientDetails(
        UUID clientId,
        String firstName,
        String lastName,
        String middleName
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1636368369469595473L;
}
