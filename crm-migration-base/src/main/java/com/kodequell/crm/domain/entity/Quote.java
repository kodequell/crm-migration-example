package com.kodequell.crm.domain.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a quote.
 * <p>
 * This domain object contains identifying and descriptive information about a quote, along with the timestamp when it
 * was created.
 * </p>
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Quote {
    private String id;
    private String name;
    private LocalDateTime created;
}
