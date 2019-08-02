package org.erhanmutlu.iyzicospringintegrationdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfiguration {

    @Bean
    public MessageChannel errorChannel() {
        return MessageChannels.queue("errorChannel").get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(500).errorChannel("errorChannel");
    }
}
