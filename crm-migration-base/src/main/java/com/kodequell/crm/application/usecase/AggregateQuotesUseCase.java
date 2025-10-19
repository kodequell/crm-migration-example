package com.kodequell.crm.application.usecase;

/**
 * Use case interface for aggregating quotes. This interface defines the business operation to aggregate quote data.
 */
public interface AggregateQuotesUseCase {

    /**
     * Performs the aggregation and publication of quotes.
     */
    int aggregateAndPublishQuotes();
}
