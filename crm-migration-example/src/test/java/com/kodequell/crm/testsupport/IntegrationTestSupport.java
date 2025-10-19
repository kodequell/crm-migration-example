package com.kodequell.crm.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodequell.crm.infrastructure.spring.Bootstrap;
import lombok.SneakyThrows;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(classes = Bootstrap.class)
public class IntegrationTestSupport implements DomainTestSupport {

    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName.parse("mockserver/mockserver:latest");

    @Container
    protected static MockServerContainer mockServerContainer = new MockServerContainer(MOCKSERVER_IMAGE);

    protected static MockServerClient mockServerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        mockServerClient = new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
        registry.add("rest.examplecrm.baseurl", mockServerContainer::getEndpoint);
    }

    @SneakyThrows
    protected String toJson(Object any) {
        return objectMapper.writeValueAsString(any);
    }
}
