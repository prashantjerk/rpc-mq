package com.example.rpcq.rpcPattern;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Profile("server")
@Service
public class RPCServer {

    private DirectExchange exchange;
    private Queue messageQ;
    private Queue converterQ;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    public RPCServer(DirectExchange exchange, Queue messageQ, Queue converterQ) {
        this.exchange = exchange;
        this.messageQ = messageQ;
        this.converterQ = converterQ;
    }

    @PostConstruct
    private void messageBind() {
        amqpAdmin.declareQueue(messageQ);
        Binding binding = BindingBuilder.bind(messageQ).to(exchange).with("messageKey");
        amqpAdmin.declareBinding(binding);
    }

    @PostConstruct
    private void converterBinding() {
        amqpAdmin.declareQueue(converterQ);
        Binding binding = BindingBuilder.bind(converterQ).to(exchange).with("converterKey");
        amqpAdmin.declareBinding(binding);
    }

    // this will listen to any queue that is named "request"
    @RabbitListener(queues = "messageQ")
    public String receiveMessage(String request) {
        System.out.println("[Request] " + request);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @RabbitListener(queues = "converterQ")
    public double receiveCurrency(float nepaliCurrency) {
        System.out.println("[Request] Nepali Currency: " + nepaliCurrency);
        return nepaliCurrency/1.6;
    }
}
