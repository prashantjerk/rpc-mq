package com.example.rpcq.rpcPattern;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("rpc")
@Configuration
public class RPCConfig {
    // this will create a directExchange named rpc
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("rpc");
    }

    @Bean
    public Queue messageQ() {
        return new Queue("authQ", true);
    }


}
