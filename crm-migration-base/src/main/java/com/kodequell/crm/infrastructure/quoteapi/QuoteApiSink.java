package com.kodequell.crm.infrastructure.quoteapi;

import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.domain.service.Sink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static com.kodequell.crm.application.dto.QuoteDto.toDto;

/**
 * Implementation of a {@link Sink} that writes {@link Quote} objects to an external Quote API using a REST POST
 * request.
 * <p>
 * This component is responsible for transforming a {@link Quote} into a DTO suitable for the API and sending it to the
 * "/quote-document" endpoint via the provided {@link RestClient}.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuoteApiSink implements Sink<Quote> {

    private final RestClient quoteApiRestClient;

    /**
     * Sends the given {@link Quote} to the external Quote API.
     * <p>
     * The method converts the quote to a DTO using {@link #toDto(Quote)} and performs a POST request to the
     * "/quote-document" endpoint. If the response status code is not 2xx, an {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param quote
     *         the {@link Quote} to be sent to the external API
     * @throws IllegalStateException
     *         if the API request fails (non-2xx status)
     */
    @Override
    public void write(Quote quote) {
        try {
            log.info("Writing to sink via HTTP POST to Quote API /quote-document endpoint with request body {}", quote);

            var response = quoteApiRestClient.post()
                                             .uri("/quote-document")
                                             .body(toDto(quote))
                                             .retrieve()
                                             .toBodilessEntity();

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException(
                        "Writing to sink via HTTP POST to Quote API /quote-document endpoint failed with status code " +
                                response.getStatusCode().value());
            }
        } catch (Exception e) {
            log.error("Writing {} to sink via HTTP POST to Quote API /quote-document endpoint failed", quote, e);
            throw new IllegalStateException(e);
        }
    }
}
