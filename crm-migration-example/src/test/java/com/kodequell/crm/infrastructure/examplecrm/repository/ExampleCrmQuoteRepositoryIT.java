package com.kodequell.crm.infrastructure.examplecrm.repository;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.testsupport.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static java.util.stream.IntStream.iterate;
import static org.assertj.core.api.Assertions.assertThat;

class ExampleCrmQuoteRepositoryIT extends IntegrationTestSupport {

    @Autowired
    private ExampleCrmQuoteRepository repository;

    @Test
    void testFindAllWithFirstPage() {
        // prepare
        expectQuoteDataPageRequest(0, iterate(0, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());

        // action
        final var result = repository.findAll(0);

        // verify
        assertThat(result.map(Quote::getName).toList()).hasSize(5)
                                                       .containsExactly(Arrays.copyOfRange(getExpectedQuoteNames(), 0, 5));
    }

    @Test
    void testFindAllWithSecondPage() {
        // prepare
        expectQuoteDataPageRequest(1, iterate(5, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());

        // action
        final var result = repository.findAll(1);

        // verify
        assertThat(result.map(Quote::getName).toList()).hasSize(5)
                                                       .containsExactly(Arrays.copyOfRange(getExpectedQuoteNames(), 5, 10));
    }

    @Test
    void testCount() {
        // prepare
        expectQuoteTotalCountRequest();

        // action
        final var result = repository.count();

        // verify
        assertThat(result).isEqualTo(10);
    }
}
