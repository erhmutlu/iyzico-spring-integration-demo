package org.erhanmutlu.iyzicospringintegrationdemo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.erhanmutlu.iyzicospringintegrationdemo.Basket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ConsumerIntegrationConfiguration {

    @Bean
    public MessageChannel consumingChannel() {
        return MessageChannels.queue("consumingChannel").get();
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(TaskExecutor taskExecutor, MessageConverter messageConverter, ActiveMQConnectionFactory consumerConnectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConcurrency("2-10");
        container.setTaskExecutor(taskExecutor);
        container.setMessageConverter(messageConverter);
        container.setConnectionFactory(consumerConnectionFactory);
        container.setDestinationName("midMQChannel");
        return container;
    }

    @Bean
    public JmsMessageDrivenEndpoint jmsMessageDrivenEndpoint(SimpleMessageListenerContainer simpleMessageListenerContainer, ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(simpleMessageListenerContainer, channelPublishingJmsMessageListener);
        endpoint.setOutputChannel(consumingChannel());
        return endpoint;
    }

    @Bean
    public IntegrationFlow endFlow() {
        return IntegrationFlows.from("consumingChannel")
                .transform(b -> {
                    ((Basket) b).setConversationId(((Basket) b).getConversationId().toUpperCase());
                    return b;
                })
                .handle(System.out::println)
                .get();
    }
}
