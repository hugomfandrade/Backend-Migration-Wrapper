package org.backend.wrapper.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
public class LegacyServiceWrapperController {

    public final static String REDIRECT_URL = "https://google.com";

    private final RestTemplate redirectRestTemplate;

    public LegacyServiceWrapperController() {

        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        this.redirectRestTemplate = new RestTemplate();
        this.redirectRestTemplate.setRequestFactory(requestFactory);
        this.redirectRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
    }

    @RequestMapping(value="**")
    public ResponseEntity<?> redirectToLegacyService(
            HttpServletRequest request,
            @RequestHeader Map<String, String> headers,
            @RequestParam Map<String,String> params) throws RestClientException {

        final ResponseEntity<?> response = redirectRestTemplate.exchange(
                REDIRECT_URL + request.getRequestURI(),
                HttpMethod.valueOf(request.getMethod()),
                new HttpEntity<>(headers),
                byte[].class,
                params
        );
        // System.err.println(response);
        return response;
    }
}  