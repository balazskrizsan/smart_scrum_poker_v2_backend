package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
@TestPropertySource(properties = "socket.is-enabled-socket-connect-and-disconnect-listeners=false")
abstract public class AbstractE2eSocketTest extends AbstractIntegrationTest
{
    @Autowired
    private InsecureKeyStoreService insecureKeyStoreService;

    @MockBean
    protected org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory securityContextFactory;

    @MockBean
    protected org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder;

    private StompSession stompSession = null;

    @AfterEach
    public void e2eAfter()
    {
        if (null != stompSession && stompSession.isConnected())
        {
            stompSession.disconnect();
        }
    }

    protected <T> StompFrameHandler buildStompFrameHandler(
        CompletableFuture<T> completableFuture,
        Class<T> payloadType
    )
    {
        return new StompFrameHandler()
        {
            @Override
            public Type getPayloadType(StompHeaders headers)
            {
                return payloadType;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload)
            {
                completableFuture.complete((T) payload);
            }
        };
    }

    protected StompSession getStompSession()
        throws Exception
    {
        UUID mockUserId = IdsUserFakeBuilder.defaultId1;
        Mockito.when(securityContextFactory.getCurrentUserId()).thenReturn(mockUserId);

        // Mock JWT decoder to return a valid JWT token
        org.springframework.security.oauth2.jwt.Jwt mockJwt = org.springframework.security.oauth2.jwt.Jwt.withTokenValue("mock-token")
            .header("alg", "none")
            .claim("sub", mockUserId.toString())
            .build();
        Mockito.when(jwtDecoder.decode(Mockito.anyString())).thenReturn(mockJwt);

        var client = new StandardWebSocketClient();
        client.getUserProperties().clear();
        client.getUserProperties().put(
            "org.apache.tomcat.websocket.SSL_CONTEXT",
            insecureKeyStoreService.getSslContext()
        );

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        var converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);

        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(converter);

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer mock-token");

        stompSession = stompClient.connectAsync(
            applicationProperties.getServerSocketFullHost(),
            (org.springframework.web.socket.WebSocketHttpHeaders) null,
            connectHeaders,
            new StompSessionHandlerAdapter()
            {
                @Override
                public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception)
                {
                    log.error("StompSessionHandlerAdapter error: " + exception.getMessage(), exception);
                }
            }
        ).get(10, SECONDS);

        return stompSession;
    }
}
