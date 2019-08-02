package org.erhanmutlu.iyzicospringintegrationdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    MessageChannel inputChannel;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        Basket basket = new Basket();
        basket.setBasketId(10L);
        basket.setPaymentId(1L);
        return (args) -> inputChannel.send(MessageBuilder.withPayload(basket).build());
    }
}
