package com.kodequell.crm.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Stream;

/**
 * Represents a generic source of data in the domain.
 * <p>
 * A {@code Source} provides objects of type {@code T}, which can be consumed by a {@link Sink} or processed by domain
 * services. The implementation may fetch data from an API, database, file, or any other external system.
 * </p>
 *
 * @param <T>
 *         the type of object this source produces
 */
@Validated
public interface Source<T> {

    /**
     * Connects to the data source and returns a stream of objects.
     * <p>
     * Each element in the stream represents a single data item produced by the source. The stream can be infinite or
     * finite depending on the implementation.
     * </p>
     *
     * @return a {@link Stream} of objects produced by the source
     */
    Stream<@NotNull @Valid T> connect();
}
