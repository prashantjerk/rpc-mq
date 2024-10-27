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

        System.out.println("Enter the app you want to connect with.");
        System.out.println("For ChatApp, type 'messageKey'");
        System.out.println("For Converter, type 'converterKey'");
        System.out.print("TYPE HERE: ");
        System.out.println();
        String key = scanner.nextLine();

        switch (key) {
            case "messageKey" -> {
                System.out.println("Connected to ChatApp.");
                String response = chatApp(key);
                if (response == null) {
                    System.out.println("RESPONSE TIMED OUT!");
                } else {
                    System.out.println("[Response]: " + response);
                }
            }
            case "converterKey" -> {
                System.out.println("Connected to Converter.");
                double indianCurrency = converter(key);
                System.out.println("[Response] Indian Currency: " + indianCurrency);
            }
            default -> System.out.println("Key does not exist!");
        }
    }

    private String chatApp(String key) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Request]: ");
        String request = scanner.nextLine();

        template.setReplyTimeout(10000);
        return (String) template.convertSendAndReceive(exchange.getName(), key, request);
    }

    private double converter(String key) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Request] Enter the Nepali Currency: ");
        float nepaliCurrency = scanner.nextFloat();
        template.setReplyTimeout(5000);
        return (double) template.convertSendAndReceive(exchange.getName(), key, nepaliCurrency);
    }

}
