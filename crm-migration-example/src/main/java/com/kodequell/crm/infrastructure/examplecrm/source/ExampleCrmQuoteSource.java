package com.kodequell.crm.infrastructure.examplecrm.source;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.domain.repository.QuoteRepository;
import com.kodequell.crm.domain.service.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExampleCrmQuoteSource implements Source<Quote> {

    private final QuoteRepository quoteRepository;

    @Override
    public Stream<Quote> connect() {
        log.info("Creating stream from quote source");

        final var totalAmount = quoteRepository.count();

        return IntStream.iterate(0, n -> n + 1)
                        .limit(totalAmount / 5)
                        .mapToObj(quoteRepository::findAll)
                        .flatMap(Collection::stream);
    }
}
