package org.erhanmutlu.iyzicospringintegrationdemo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ProducerIntegrationConfiguration {

    @Bean
    public MessageChannel inputChannel() {
        return MessageChannels.queue("inputChannel").get();
    }

    @Bean
    public MessageChannel midChannel() {
        return MessageChannels.queue("midChannel").get();
    }

//    @Bean
//    @ServiceActivator(inputChannel = "midChannel")
//    public MessageHandler jmsMessageHandler(JmsTemplate jmsTemplate) {
//        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
//        handler.setDestinationName("midMQChannel");
//        return handler;
//    }

    @Bean
    @ServiceActivator(inputChannel="midChannel")
    public JmsOutboundGateway jmsGateway(ActiveMQConnectionFactory producerConnectionFactory, MessageConverter messageConverter) {
        JmsOutboundGateway gw = new JmsOutboundGateway();
        gw.setConnectionFactory(producerConnectionFactory);
        gw.setRequestDestinationName("midMQChannel");
        gw.setMessageConverter(messageConverter);
//        gw.setReplyDestinationName("test.in");
        gw.setCorrelationKey("JMSCorrelationID");
        return gw;
    }

    @Bean
    public IntegrationFlow producerFlow() {
        return IntegrationFlows.from("inputChannel")
                .channel("midChannel")
                .get();
    }

//    @Bean
//    public IntegrationFlow myFlow() {
//        return IntegrationFlows.from()
//                .channel("inputChannel")
//                .filter((Integer p) -> p > 0)
//                .transform(Object::toString)
//                .channel(MessageChannels.queue())
//                .get();
//    }
}
