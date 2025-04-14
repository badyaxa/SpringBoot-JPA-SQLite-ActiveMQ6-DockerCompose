package com.pr.service1web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.service1web.service.AuthService;
import com.pr.service1web.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class ClientControllerTest {

    private static final String VALID_BEARER_TOKEN = "Bearer valid-token";
    private static final String INVALID_BEARER_TOKEN = "Bearer invalid-token";
    private static final String TEST_USER_ID = "test-user-123";
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private ClientService clientService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private ClientController clientController;
    private UUID validClientId;

    @BeforeEach
    public void setUp() {
        validClientId = UUID.randomUUID();

        when(authService.isValidSessionId(VALID_BEARER_TOKEN)).thenReturn(true);
        when(authService.isValidSessionId(INVALID_BEARER_TOKEN)).thenReturn(false);
        when(authService.generateBearerToken(TEST_USER_ID)).thenReturn("valid-token");

        doNothing().when(clientService).process(any(UUID.class));

        mockMvc = MockMvcBuilders
                .standaloneSetup(clientController)
                .build();
    }

    @Test
    public void testProcessWithValidAuth() throws Exception {
        ClientRequest request = new ClientRequest(validClientId);

        mockMvc.perform(post("/api/client/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, VALID_BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Client processing initiated"));

        verify(authService, times(1)).isValidSessionId(VALID_BEARER_TOKEN);
        verify(clientService, times(1)).process(validClientId);
    }

    @Test
    public void testProcessWithInvalidAuth() throws Exception {
        ClientRequest request = new ClientRequest(validClientId);

        mockMvc.perform(post("/api/client/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, INVALID_BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid session ID"));

        verify(authService, times(1)).isValidSessionId(INVALID_BEARER_TOKEN);
        verify(clientService, never()).process(any(UUID.class));
    }

    @Test
    public void testProcessWithMissingAuth() throws Exception {
        ClientRequest request = new ClientRequest(validClientId);

        mockMvc.perform(post("/api/client/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).isValidSessionId(anyString());
        verify(clientService, never()).process(any(UUID.class));
    }

    @Test
    public void testProcessWithInvalidRequestBody() throws Exception {
        String invalidJson = "{\"clientId\": \"not-a-valid-uuid\"}";

        mockMvc.perform(post("/api/client/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, VALID_BEARER_TOKEN)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(clientService, never()).process(any(UUID.class));
    }

    @Test
    public void testCreateSessionSuccess() throws Exception {
        mockMvc.perform(post("/api/client/login")
                        .param("userId", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("Bearer valid-token"));

        verify(authService, times(1)).generateBearerToken(TEST_USER_ID);
    }

    @Test
    public void testCreateSessionWithoutUserId() throws Exception {
        mockMvc.perform(post("/api/client/login"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).generateBearerToken(anyString());
    }
}
