package com.kodequell.crm.infrastructure.examplecrm;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.infrastructure.examplecrm.source.ExampleCrmQuoteSource;
import com.kodequell.crm.infrastructure.examplecrm.repository.ExampleCrmQuoteRepository.QuoteData;
import com.kodequell.crm.testsupport.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.IntStream.iterate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class ExampleCrmQuoteDataSourceIT extends IntegrationTestSupport {

    @Autowired
    private ExampleCrmQuoteSource exampleCrmQuoteSource;

    @Test
    void testConnect() {
        // prepare
        expectQuoteTotalCountRequest();
        expectQuoteDataPageRequest(0, iterate(0, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());
        expectQuoteDataPageRequest(1, iterate(5, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());

        // action
        final var result = exampleCrmQuoteSource.connect();

        // verify
        assertThat(result.map(Quote::getName).toList()).hasSize(10).containsExactly(getExpectedQuoteNames());
    }

    private static String[] getExpectedQuoteNames() {
        return iterate(0, n -> n + 1)
                .limit(10)
                .mapToObj(ExampleCrmQuoteDataSourceIT::createQuoteItemName)
                .toArray(String[]::new);
    }

    private static String createQuoteItemName(int n) {
        return "Quote_" + (n + 1);
    }

    private void expectQuoteDataPageRequest(int page, List<QuoteData> responseList) {
        mockServerClient.when(request()
                                .withMethod("GET")
                                .withPath("/quote/")
                                .withQueryStringParameter("page", String.valueOf(page))
                                .withQueryStringParameter("size", "5"))
                        .respond(response()
                                .withStatusCode(200)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody(toJson(responseList))
                        );
    }

    private void expectQuoteTotalCountRequest() {
        mockServerClient.when(request().withMethod("GET").withPath("/quote/count"))
                        .respond(response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(toJson(defaultQuotesTotalAmount())));
    }

    private QuoteData createQuoteData(int n) {
        return defautQuoteItem(createQuoteItemName(n));
    }
}
