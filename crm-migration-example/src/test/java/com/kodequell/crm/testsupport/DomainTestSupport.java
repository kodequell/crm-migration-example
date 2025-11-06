package com.kodequell.crm.testsupport;

import com.kodequell.crm.infrastructure.examplecrm.repository.ExampleCrmQuoteRepository.QuoteData;
import com.kodequell.crm.infrastructure.examplecrm.repository.ExampleCrmQuoteRepository.QuotesTotalAmount;

import java.util.UUID;

public interface DomainTestSupport {

    default QuotesTotalAmount defaultQuotesTotalAmount() {
        return new QuotesTotalAmount(10);
    }

    default QuoteData defautQuoteItem(String name) {
        return new QuoteData(UUID.randomUUID().toString(), name);
    }
}
