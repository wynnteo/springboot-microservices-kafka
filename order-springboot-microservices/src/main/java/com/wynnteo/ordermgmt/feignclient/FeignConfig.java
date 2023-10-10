package com.wynnteo.ordermgmt.feignclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    // @Value("${keycloak.scope}")
    // private String scope;

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            String token = obtainAccessToken();
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        };
    }

    private String obtainAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        //formData.add("scope", scope);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(tokenUri, formData, JsonNode.class);
        JsonNode responseBody = response.getBody();

        return responseBody != null ? responseBody.get("access_token").asText() : null;
    }
}
