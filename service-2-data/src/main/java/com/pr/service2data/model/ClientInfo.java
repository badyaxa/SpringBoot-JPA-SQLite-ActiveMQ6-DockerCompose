package com.pr.service2data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientInfo(UUID clientId) implements Serializable {
    @Serial
    private static final long serialVersionUID = -3285134464861943997L;
}
