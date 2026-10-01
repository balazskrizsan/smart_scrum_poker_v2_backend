package org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;

@Schema(description = "Empty payload for operations that don't require input")
@JsonDeserialize(using = EmptyPayload.EmptyPayloadDeserializer.class)
public record EmptyPayload()
{
    public static class EmptyPayloadDeserializer extends JsonDeserializer<EmptyPayload>
    {
        @Override
        public EmptyPayload deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            return new EmptyPayload();
        }
    }
}
