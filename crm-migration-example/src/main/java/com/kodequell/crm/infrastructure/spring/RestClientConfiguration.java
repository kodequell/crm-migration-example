package com.kodequell.crm.infrastructure.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Value("${rest.examplecrm.baseurl}")
    private String exampleCrmBaseUrl;

    @Bean
    public RestClient exampleCrmRestClient() {
        return RestClient.builder().baseUrl(exampleCrmBaseUrl).build();
    }
}
