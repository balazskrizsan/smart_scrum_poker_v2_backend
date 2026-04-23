package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;


@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = false, level = PRIVATE)
abstract public class AbstractIdsApiRepository
{
    RestClient restClient;

    @Autowired
    public void setRestClient(RestClient restClient)
    {
        this.restClient = restClient;
    }

    public @NonNull <REQ, RES> List<RES> getOrThrow(
        @NonNull final String uri,
        REQ request,
        @NonNull ParameterizedTypeReference<List<RES>> typeReference
    )
    {
        return Objects.requireNonNull(
            restClient.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(request)
                .retrieve()
                .body(typeReference)
        );
    }
}
