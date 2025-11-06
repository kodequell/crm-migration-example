package com.kodequell.crm.infrastructure.examplecrm.repository;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.domain.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * Implementation of {@link QuoteRepository} that retrieves {@link Quote} entities from an external Example CRM system
 * via REST API.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ExampleCrmQuoteRepository implements QuoteRepository {

    private final RestClient exampleCrmRestClient;

    /**
     * Retrieves a stream of {@link Quote} entities for the specified page by making a REST request to the external CRM
     * system.
     * <p>
     * Each {@link Quote} object is mapped from {@link QuoteData} returned by the API. If an exception occurs during the
     * API call, an empty stream is returned.
     * </p>
     *
     * @param page
     *         the page number to fetch, must be positive
     * @return a {@link Stream} of {@link Quote} entities, or an empty stream if the request fails
     */
    @Override
    public List<Quote> findAll(int page) {
        try {
            log.info("Requesting quotes page {}", page);

            return exampleCrmRestClient
                    .get()
                    .uri(createUriForPageFetch(page))
                    .retrieve().<List<QuoteData>>body(new ParameterizedTypeReference<>() {})
                    .stream()
                    .map(ExampleCrmQuoteRepository::convert)
                    .toList();
        } catch (Exception e) {
            log.error("Fetch quotes data page failed", e);
            return List.of();
        }
    }

    /**
     * Retrieves the total number of quotes from the external CRM system.
     * <p>
     * Returns zero if the request fails or an exception occurs.
     * </p>
     *
     * @return total number of quotes, or zero if the request fails
     */
    @Override

    public int count() {
        try {
            log.info("Requesting quotes total amount");

            final var response = exampleCrmRestClient.get()
                                                     .uri("/quote/count")
                                                     .retrieve()
                                                     .toEntity(QuotesTotalAmount.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Requesting quotes total amount failed with " +
                        response.getStatusCode().value() + " status code");
            }

            return Optional.ofNullable(response.getBody()).map(value -> value.totalAmount).orElse(0);
        } catch (Exception e) {
            log.error("GET quotes total amount request failed", e);
            return 0;
        }
    }

    private static String createUriForPageFetch(int page) {
        return fromPath("/quote/").queryParam("page", page).queryParam("size", "5").toUriString();
    }

    private static Quote convert(QuoteData item) {
        return Quote.builder().id(item.id).name(item.name).created(LocalDateTime.now()).build();
    }

    public record QuotesTotalAmount(int totalAmount) {}

    public record QuoteData(String id, String name) {}
}
