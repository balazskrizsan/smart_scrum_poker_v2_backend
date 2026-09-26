package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses;

import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Utility class for creating socket response wrappers from ResponseData.
 * This centralizes the common constructor logic used across all listener response records.
 */
public final class SocketResponseFactory
{
    private SocketResponseFactory() {}

    /**
     * Creates a SocketResponseWrapper implementation from ResponseData.
     * This is a convenience method to avoid repeating the constructor logic in each listener.
     *
     * @param responseData the ResponseData to convert
     * @param factory a function that creates the specific SocketResponseWrapper implementation
     * @param <T> the response data type
     * @param <R> the SocketResponseWrapper implementation type
     * @return the created SocketResponseWrapper
     */
    public static <T, R extends SocketResponseWrapper<T>> R fromResponseData(
        ResponseData<T> responseData,
        ResponseDataFactory<T, R> factory
    ) {
        return factory.create(
            responseData.data(),
            responseData.success(),
            responseData.errorCode(),
            responseData.requestId(),
            responseData.socketResponseDestination()
        );
    }

    /**
     * Creates a ResponseEntity wrapper structure from ResponseEntity<ResponseData<T>>.
     * This converts Spring's ResponseEntity to a non-generic record structure for springwolf compatibility.
     *
     * @param responseEntity the ResponseEntity to convert
     * @param innerFactory a function that creates the inner ResponseData wrapper
     * @param outerFactory a function that creates the outer ResponseEntity wrapper
     * @param <T> the response data type
     * @param <I> the inner ResponseData wrapper type
     * @param <O> the outer ResponseEntity wrapper type
     * @return the created outer wrapper
     */
    public static <T, I, O> O fromResponseEntity(
        ResponseEntity<ResponseData<T>> responseEntity,
        ResponseDataFactory<T, I> innerFactory,
        ResponseEntityFactory<I, O> outerFactory
    ) {
        HttpStatus httpStatus = HttpStatus.valueOf(responseEntity.getStatusCode().value());
        ResponseData<T> responseData = responseEntity.getBody();

        I inner = innerFactory.create(
            responseData.data(),
            responseData.success(),
            responseData.errorCode(),
            responseData.requestId(),
            responseData.socketResponseDestination()
        );

        return outerFactory.create(
            inner,
            responseEntity.getHeaders().toSingleValueMap(),
            httpStatus.getReasonPhrase(),
            responseEntity.getStatusCodeValue()
        );
    }

    @FunctionalInterface
    public interface ResponseDataFactory<T, R>
    {
        R create(T data, Boolean success, int errorCode, String requestId, String socketResponseDestination);
    }

    @FunctionalInterface
    public interface ResponseEntityFactory<I, O>
    {
        O create(I body, Map<String, String> headers, String statusCode, int statusCodeValue);
    }
}
