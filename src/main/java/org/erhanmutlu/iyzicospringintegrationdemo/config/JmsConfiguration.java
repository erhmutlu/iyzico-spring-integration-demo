package org.erhanmutlu.iyzicospringintegrationdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.erhanmutlu.iyzicospringintegrationdemo.config.properties.JmsConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
public class JmsConfiguration {

    private static final Integer CORE_POOL_SIZE = 10;

    private final JmsConfigProperties jmsConfigProperties;

    public JmsConfiguration(JmsConfigProperties jmsConfigProperties) {
        this.jmsConfigProperties = jmsConfigProperties;
    }

    @Bean
    public JmsTemplate jmsTemplate(ObjectMapper objectMapper) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(producerConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter(objectMapper));
        return jmsTemplate;
    }

    @Bean
    public ActiveMQConnectionFactory producerConnectionFactory() {
        return prepareActiveMQConnectionFactory();
    }

//    @Bean
//    public ActiveMQConnectionFactory consumerConnectionFactory() {
//        return prepareActiveMQConnectionFactory();
//    }

//    @Bean
//    public JmsListenerContainerFactory<?> jmsContainerFactory(ObjectMapper objectMapper) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(consumerConnectionFactory());
//        factory.setTaskExecutor(executor());
//        factory.setConcurrency("2-10");
//        factory.setMessageConverter(jacksonJmsMessageConverter(objectMapper));
//        return factory;
//    }

//    @Bean
//    public Executor executor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(CORE_POOL_SIZE);
//        executor.initialize();
//        return executor;
//    }

    @Bean
    @Primary
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setTargetType(MessageType.TEXT);

        messageConverter.setTypeIdPropertyName("_type");

        HashMap<String, Class<?>> idMapping = new HashMap<>();
//        idMapping.put(WebhookMessage.class.getName(), WebhookMessage.class);
        messageConverter.setTypeIdMappings(idMapping);

        return messageConverter;
    }

//    @Bean
//    public JmsMessageDrivenEndpoint outputChannel() {
//        return Jms.messageDrivenChannelAdapter(prepareActiveMQConnectionFactory())
//                .configureListenerContainer(defaultMessageListenerContainerJmsListenerContainerSpec ->
//                        defaultMessageListenerContainerJmsListenerContainerSpec.sessionTransacted(true))
//                .destination("outputChannel")
//                .get();
//    }

    private ActiveMQConnectionFactory prepareActiveMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(jmsConfigProperties.getBrokerUrl());
        Properties mqProperties = new Properties();
        mqProperties.put("in-memory", jmsConfigProperties.getInMemory());
        mqProperties.put("pooled", jmsConfigProperties.getPooled());
        factory.setProperties(mqProperties);
        return factory;
    }
}
