package com.kodequell.crm.domain.service;

/**
 * Represents a generic destination for data in the domain.
 * <p>
 * A {@code Sink} consumes objects of type {@code T} and can implement any form of output, such as writing to a
 * database, sending over a network, writing to a file, or publishing to a message queue.
 * </p>
 *
 * @param <T>
 *         the type of object this sink consumes
 */
public interface Sink<T> {

    /**
     * Writes or sends a single object to the sink.
     * <p>
     * The implementation defines how the object is persisted, transmitted, or otherwise handled.
     * </p>
     *
     * @param object
     *         the object to write to the sink
     */
    void write(T object);
}
