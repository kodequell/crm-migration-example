package com.kodequell.crm.infrastructure.examplecrm;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.infrastructure.examplecrm.source.ExampleCrmQuoteSource;
import com.kodequell.crm.testsupport.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.IntStream.iterate;
import static org.assertj.core.api.Assertions.assertThat;

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
}
