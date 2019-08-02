package org.erhanmutlu.iyzicospringintegrationdemo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.activemq")
public class JmsConfigProperties {

    private String brokerUrl;
    private String inMemory;
    private String pooled;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getInMemory() {
        return inMemory;
    }

    public void setInMemory(String inMemory) {
        this.inMemory = inMemory;
    }

    public String getPooled() {
        return pooled;
    }

    public void setPooled(String pooled) {
        this.pooled = pooled;
    }
}
