package com.kodequell.crm.infrastructure.jmx;

import com.kodequell.crm.testsupport.IntegrationTestSupport;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.iterate;
import static org.mockserver.matchers.MatchType.ONLY_MATCHING_FIELDS;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

public class QuoteMigrationJobIT extends IntegrationTestSupport {

    @Autowired
    private QuoteMigrationJob quoteMigrationJob;

    @SneakyThrows
    @Test
    void testStartMigration() {
        // prepare
        // (SOURCE) Expect total count request
        expectQuoteTotalCountRequest();
        // (SOURCE) Expect get page 0 request
        expectQuoteDataPageRequest(0, iterate(0, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());
        // (SOURCE) Expect get page 1 request
        expectQuoteDataPageRequest(1, iterate(5, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());
        // (SINK) Expect 10 quote api requests
        stream(getExpectedQuoteNames()).forEach(QuoteMigrationJobIT::expectQuoteApiRequest);

        // action
        quoteMigrationJob.startMigration();
        Thread.sleep(5000L);
    }

    private static void expectQuoteApiRequest(String quoteName) {
        mockServerClient.when(request()
                                .withMethod("POST")
                                .withPath("/quote-document")
                                .withBody(json("{ \"name\": \"" + quoteName + "\" }", ONLY_MATCHING_FIELDS)), exactly(1)).
                        respond(response().withStatusCode(200));
    }
}
