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
    private void binding() {
        amqpAdmin.declareQueue(messageQ);
        Binding binding = BindingBuilder.bind(messageQ).to(exchange).with("authKey");
        amqpAdmin.declareBinding(binding);
    }

    // this will listen to any queue that is named "request"
    @RabbitListener(queues = "authQ")
    public String receiveMessage(long cardNum) {
        System.out.println("[Request]: " + cardNum);
        Scanner scanner = new Scanner(System.in);
        String message;
        if(scanner.nextLine().equals("auth")) {

        }

    }

}
