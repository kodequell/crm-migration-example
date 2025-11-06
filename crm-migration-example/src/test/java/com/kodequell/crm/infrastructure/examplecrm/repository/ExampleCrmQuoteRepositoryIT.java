package com.kodequell.crm.infrastructure.examplecrm.repository;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.infrastructure.examplecrm.repository.ExampleCrmQuoteRepository.QuoteData;
import com.kodequell.crm.testsupport.IntegrationTestSupport;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.IntStream.iterate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(result.stream().map(Quote::getName).toList()).hasSize(5)
                                                                .containsExactly(Arrays.copyOfRange(getExpectedQuoteNames(), 0, 5));
    }

    @Test
    void testFindAllWithSecondPage() {
        // prepare
        expectQuoteDataPageRequest(1, iterate(5, n -> n + 1).limit(5).mapToObj(this::createQuoteData).toList());

        // action
        final var result = repository.findAll(1);

        // verify
        assertThat(result.stream().map(Quote::getName).toList()).hasSize(5)
                                                                .containsExactly(Arrays.copyOfRange(getExpectedQuoteNames(), 5, 10));
    }

    @Test
    void testFindAllWithInvalidResponse() {
        // prepare
        expectQuoteDataPageRequest(0, List.of(new QuoteData(null, null)));

        // action
        assertThatThrownBy(() -> repository.findAll(0)).isInstanceOf(ConstraintViolationException.class);
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
