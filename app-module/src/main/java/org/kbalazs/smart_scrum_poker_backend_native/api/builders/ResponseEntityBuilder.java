package org.kbalazs.smart_scrum_poker_backend_native.api.builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseEntityBuilder<T>
{
    T data;
    int errorCode = 0;
    HttpStatus statusCode = HttpStatus.OK;
    HttpHeaders headers = new HttpHeaders();
    SocketDestination socketDestination = null;

    public ResponseEntity<ResponseData<T>> build() throws ApiException
    {
        Boolean success = errorCode == 0;

        if (errorCode > 0 && statusCode == HttpStatus.OK)
        {
            throw new ApiException("Status code setup is needed for error response");
        }

        ResponseData<T> responseData = new ResponseData<>(data, success, errorCode, "1", getDestination());

        return new ResponseEntity<>(responseData, headers, statusCode);
    }

    private String getDestination()
    {
        return socketDestination == null ? "" : socketDestination.getValue();
    }

    public void downloadAsCsv(String fileName)
    {
        headers.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
        headers.set("Content-Disposition", "attachment; filename=" + fileName + ".csv");
    }
}
