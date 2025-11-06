package com.kodequell.crm.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
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
    
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @PastOrPresent
    private LocalDateTime created;
}
