package ru.vorobyev.tracker.web;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Strings;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class CheckHealthResponseErrorHandler implements ResponseErrorHandler {

    private final RestOperations restTemplate;
    private final RestClient restClient;

    public CheckHealthResponseErrorHandler(RestOperations restTemplate, RestClient restClient) {
        this.restTemplate = restTemplate;
        this.restClient = restClient;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        if (response.getStatusCode().value() != 200) {
            String str = Strings.fromByteArray(response.getBody().readAllBytes());
            log.warn("Code: {}, text: {}", response.getStatusCode().value(), str);

            return true;
        }

        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) {
        restTemplate.postForLocation(URI.create(restClient.getUrl().concat(restClient.getBasePath()).concat("/shutdown")), null);
    }
}
