package com.kodequell.crm.infrastructure.jmx;

import com.kodequell.crm.application.usecase.AggregateQuotesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Spring component responsible for executing the quote migration process.
 * <p>
 * This class exposes a JMX-managed resource that allows triggering the migration via JMX operations. The migration is
 * executed asynchronously using a single-threaded executor.
 * </p>
 */
@Component
@ManagedResource(objectName = "com.kodequell:type=QuoteMigrationJob", description = "Quote migration execution")
@RequiredArgsConstructor
@Slf4j
public class QuoteMigrationJob {

    private static final ExecutorService QUEUE = Executors.newSingleThreadExecutor();

    private final AggregateQuotesUseCase aggregateQuotesUseCase;

    /**
     * Starts the quote migration asynchronously.
     * <p>
     * This operation submits the aggregation and publishing of quotes to a single-threaded executor. The actual
     * processing is handled by {@link AggregateQuotesUseCase#aggregateAndPublishQuotes()}.
     * </p>
     * <p>
     * The method returns immediately after submitting the task; it does not block until completion.
     * </p>
     */
    @ManagedOperation(description = "Start quotes migration")
    public void startMigration() {
        log.info("Starting quotes data migration task");
        QUEUE.submit(aggregateQuotesUseCase::aggregateAndPublishQuotes);
    }
}
