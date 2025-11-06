package com.kodequell.crm.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodequell.crm.infrastructure.examplecrm.repository.ExampleCrmQuoteRepository.QuoteData;
import com.kodequell.crm.infrastructure.spring.Bootstrap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.IntStream.iterate;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        registry.add("rest.quoteapi.baseurl", mockServerContainer::getEndpoint);
    }

    @AfterEach
    protected void printExpectations() {
        final var activeExpectations = mockServerClient.retrieveActiveExpectations(null);
        if (activeExpectations.length > 0) {
            System.err.println("Active expectations:");
            Arrays.stream(activeExpectations).forEach(System.err::println);
            System.err.println("Recorded requests:");
            Arrays.stream(mockServerClient.retrieveRecordedRequests(null)).forEach(System.err::println);
            Assertions.fail("MockServer expectations should be empty at this point");
        }
        mockServerClient.reset();
    }

    protected void expectQuoteDataPageRequest(int page, List<QuoteData> responseList) {
        mockServerClient.when(request()
                                .withMethod("GET")
                                .withPath("/quote/")
                                .withQueryStringParameter("page", String.valueOf(page))
                                .withQueryStringParameter("size", "5"), exactly(1))
                        .respond(response()
                                .withStatusCode(200)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody(toJson(responseList))
                        );
    }

    protected void expectQuoteTotalCountRequest() {
        mockServerClient.when(request().withMethod("GET").withPath("/quote/count"), exactly(1))
                        .respond(response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(toJson(defaultQuotesTotalAmount())));
    }

    protected static String[] getExpectedQuoteNames() {
        return iterate(0, n -> n + 1)
                .limit(10)
                .mapToObj(IntegrationTestSupport::createQuoteItemName)
                .toArray(String[]::new);
    }

    protected static String createQuoteItemName(int n) {
        return "Quote_" + (n + 1);
    }

    protected QuoteData createQuoteData(int n) {
        return defaultQuoteData(createQuoteItemName(n));
    }

    @SneakyThrows
    protected String toJson(Object any) {
        return objectMapper.writeValueAsString(any);
    }
}
