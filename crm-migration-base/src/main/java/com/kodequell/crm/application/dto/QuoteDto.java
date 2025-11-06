package com.kodequell.crm.application.dto;

import com.kodequell.crm.domain.entity.Quote;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record QuoteDto(@NotBlank String id, @NotBlank String name, @PastOrPresent LocalDateTime created) {

    public static QuoteDto toDto(Quote quote) {
        return new QuoteDto(quote.getId(), quote.getName(), quote.getCreated());
    }
}
