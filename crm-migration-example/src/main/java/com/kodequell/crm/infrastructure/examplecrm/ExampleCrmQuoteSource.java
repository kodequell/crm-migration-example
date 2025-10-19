package com.kodequell.crm.infrastructure.examplecrm;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.domain.service.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExampleCrmQuoteSource implements Source<Quote> {

    private final RestClient exampleCrmRestClient;

    @Override
    public Stream<Quote> connect() {
        log.info("Creating stream from quote source");
        return Optional.ofNullable(requestQuotesTotalAmount()).map(this::aggregateQuoteDatePages).orElse(Stream.of());
    }

    private QuotesTotalAmount requestQuotesTotalAmount() {
        try {
            log.info("Requesting quotes total amount");

            final var response = exampleCrmRestClient.
                    get().uri("/quote/count").retrieve().toEntity(QuotesTotalAmount.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Requesting quotes total amount failed with " +
                        response.getStatusCode().value() + " status code");
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("GET quotes total amount request failed", e);
            return null;
        }
    }

    private Stream<Quote> aggregateQuoteDatePages(QuotesTotalAmount totalAmountResponse) {

        var totalAmount = totalAmountResponse.totalAmount;
        log.info("Requesting {} total quotes in batches of 5", totalAmount);

        return IntStream.iterate(0, n -> n + 1)
                        .limit(totalAmount / 5)
                        .mapToObj(this::fetchPage)
                        .flatMap(List::stream)
                        .map(item -> Quote.builder().id(item.id).name(item.name).created(LocalDateTime.now()).build());
    }

    private List<QuoteData> fetchPage(int page) {
        try {
            var uri = createUriForPageFetch(page);
            log.info("Fetching quote data from {}", uri);

            List<QuoteData> quotes = exampleCrmRestClient.
                    get().uri(uri).retrieve().body(new ParameterizedTypeReference<>() {});

            log.info("Fetched: {}", quotes);

            return quotes;
        } catch (Exception e) {
            log.error("Fetch quotes data page failed", e);
            return List.of();
        }
    }

    private static String createUriForPageFetch(int page) {
        return fromPath("/quote/").queryParam("page", page).queryParam("size", "5").toUriString();
    }

    public record QuotesTotalAmount(int totalAmount) { }

    public record QuoteData(String id, String name) { }
}
