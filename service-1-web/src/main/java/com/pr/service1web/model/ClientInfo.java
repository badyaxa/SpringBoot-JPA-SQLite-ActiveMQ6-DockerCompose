package com.pr.service1web.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record ClientInfo(UUID clientId) implements Serializable {
    @Serial
    private static final long serialVersionUID = -3285134464861943997L;
}
