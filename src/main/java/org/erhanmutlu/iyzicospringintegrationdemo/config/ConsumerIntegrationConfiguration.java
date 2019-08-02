package org.erhanmutlu.iyzicospringintegrationdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
public class ConsumerIntegrationConfiguration {

    @Bean
    public DirectChannel consumingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return MessageChannels.queue("outputChannel").get();
    }

    @Bean
    public IntegrationFlow endFlow() {
        return IntegrationFlows.from("consumingChannel")
                .handle(System.out::println)
                .get();
    }

    @Bean
    public JmsMessageDrivenEndpoint jmsMessageDrivenEndpoint(
            ConnectionFactory connectionFactory) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
                simpleMessageListenerContainer(connectionFactory),
                channelPublishingJmsMessageListener());
        endpoint.setOutputChannel(consumingChannel());
        return endpoint;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(
            ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName("midMQChannel");
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener() {
        return new ChannelPublishingJmsMessageListener();
    }
}
