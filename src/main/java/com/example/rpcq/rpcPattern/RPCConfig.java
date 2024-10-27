package com.example.rpcq.rpcPattern;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
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
        return new Queue("messageQ");
    }

    @Bean
    public Queue calcQ() {
        return new Queue("calcQ");
    }

    @Bean
    public Queue converterQ() {
        return new Queue("converterQ");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public RabbitTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RPCServer rpcServer) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(rpcServer, "receiveCalc");
        adapter.setMessageConverter(messageConverter());
        return adapter;
    }
}
