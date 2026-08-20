package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
abstract public class AbstractE2eSocketTest extends AbstractIntegrationTest
{
    @Autowired
    private InsecureKeyStoreService insecureKeyStoreService;

    private StompSession stompSession = null;

    @AfterEach
    public void e2eAfter()
    {
        if (null != stompSession)
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

    protected StompSession getStompSession() throws Exception
    {
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

        stompSession = stompClient.connectAsync(
            applicationProperties.getServerSocketFullHost(),
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
