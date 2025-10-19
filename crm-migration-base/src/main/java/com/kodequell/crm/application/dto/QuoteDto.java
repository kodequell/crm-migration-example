package com.kodequell.crm.application.dto;

import com.kodequell.crm.domain.entity.Quote;

import java.time.LocalDateTime;

public record QuoteDto(String id, String name, LocalDateTime created) {

    public static QuoteDto toDto(Quote quote) {
        return new QuoteDto(quote.getId(), quote.getName(), quote.getCreated());
    }
}
