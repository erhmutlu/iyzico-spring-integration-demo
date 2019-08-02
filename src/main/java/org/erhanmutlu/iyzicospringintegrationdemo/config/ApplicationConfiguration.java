package org.erhanmutlu.iyzicospringintegrationdemo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@ComponentScan
@EnableJms
public class ApplicationConfiguration {
}
