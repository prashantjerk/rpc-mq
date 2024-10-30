package com.example.rpcq.rpcPattern;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Profile("client")
@Service
public class RPCClient {
    @Autowired
    private RabbitTemplate template;

    private final DirectExchange exchange;


    @Autowired
    public RPCClient(DirectExchange exchange) {
        this.exchange = exchange;
    }

    public void send() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the card number: ");

        System.out.println();
        long cardNumber = scanner.nextLong();

        auth(cardNumber);
    }

    private String auth(long cardNum) {


        template.setReplyTimeout(10000);
        return (String) template.convertSendAndReceive(exchange.getName(), "authKey", cardNum);
    }
}
