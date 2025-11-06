package com.kodequell.crm.infrastructure.spring;

import com.kodequell.crm.application.service.MigrationService;
import com.kodequell.crm.application.usecase.AggregateQuotesUseCase;
import com.kodequell.crm.domain.entity.Quote;
import com.kodequell.crm.domain.service.Sink;
import com.kodequell.crm.domain.service.Source;
import com.kodequell.crm.infrastructure.jmx.QuoteMigrationJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnBean(Source.class)
public class MigrationAutoConfiguration {

    @Value("${rest.quoteapi.baseurl}")
    private String quoteApiBaseUrl;

    @Bean
    public RestClient quoteApiRestClient() {
        return RestClient.builder().baseUrl(quoteApiBaseUrl).build();
    }

    @Bean
    public MigrationService migrationService(Source<Quote> quoteSource, Sink<Quote> quoteSink) {
        return new MigrationService(quoteSource, quoteSink);
    }

    @Bean
    public QuoteMigrationJob quoteMigrationJob(AggregateQuotesUseCase aggregateQuotesUseCase) {
        return new QuoteMigrationJob(aggregateQuotesUseCase);
    }
}
