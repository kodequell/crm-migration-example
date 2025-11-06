package com.kodequell.crm.domain.repository;

import com.kodequell.crm.domain.entity.Quote;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Stream;

/**
 * Repository interface for managing {@link Quote} entities.
 * <p>
 * Provides methods to retrieve quotes and to get the total count of quotes.
 * </p>
 */
@Validated
public interface QuoteRepository {

    /**
     * Retrieves a stream of all {@link Quote} entities for the specified page number.
     * <p>
     * The returned stream elements are guaranteed to be non-null and valid according to the {@link Quote} validation
     * constraints.
     * </p>
     *
     * @param page
     *         the page number to retrieve, must be positive
     * @return a stream of {@link Quote} entities
     */
    Stream<@NotNull @Valid Quote> findAll(@Positive int page);

    /**
     * Returns the total number of {@link Quote} entities.
     * <p>
     * The returned count is guaranteed to be zero or a positive integer.
     * </p>
     *
     * @return the total number of quotes, zero or positive
     */
    @PositiveOrZero
    int count();
}
