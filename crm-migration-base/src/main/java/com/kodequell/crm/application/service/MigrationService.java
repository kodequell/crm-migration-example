package com.kodequell.crm.application.service;

import com.kodequell.crm.application.usecase.AggregateQuotesUseCase;
import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.domain.service.Sink;
import com.kodequell.crm.domain.service.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of the {@link AggregateQuotesUseCase} that migrates or aggregates quotes from a source to a sink.
 * <p>
 * This service acts as the Application Layer orchestrator, connecting the configured {@link Source} to the {@link Sink}
 * and processing each quote.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationService implements AggregateQuotesUseCase {

    private final Source<Quote> quoteSource;
    private final Sink<Quote> quoteSink;

    /**
     * Aggregates quotes from the configured data source and publishes them.
     * <p>
     * This method connects to the {@link #quoteSource} and retrieves all available quotes. Each retrieved quote is then
     * written to the {@link #quoteSink}.
     * </p>
     * <p>
     * Note: This method executes synchronously and will block until all quotes have been read from the source and
     * written to the sink.
     * </p>
     */
    @Override
    public int aggregateAndPublishQuotes() {
        log.info("Connecting quotes from source to sink");
        final var success = new AtomicInteger(0);
        final var failed = new AtomicInteger(0);
        quoteSource.connect().forEach(quote -> writeToSink(quote, success, failed));
        log.info("{} quotes have been published, {} have been failed", success.get(), failed.get());
        return success.get();
    }

    private void writeToSink(Quote quote, AtomicInteger success, AtomicInteger failed) {
        try {
            quoteSink.write(quote);
            success.incrementAndGet();
        } catch (Exception e) {
            log.error("Writing {} to {} failed", quote, quoteSink.getClass().getSimpleName(), e);
            failed.incrementAndGet();
        }
    }
}
