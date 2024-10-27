package com.example.rpcq.rpcPattern;

import com.example.rpcq.numbers.CalcNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Profile({"server", "client"})
@Service
public class RPCServer {

    private DirectExchange exchange;
    private Queue messageQ;
    private Queue calcQ;
    private Queue converterQ;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    public RPCServer(DirectExchange exchange, Queue messageQ, Queue calcQ, Queue converterQ) {
        this.exchange = exchange;
        this.messageQ = messageQ;
        this.calcQ = calcQ;
        this.converterQ = converterQ;
    }

    @PostConstruct
    private void messageBind() {
        amqpAdmin.declareQueue(messageQ);
        Binding binding = BindingBuilder.bind(messageQ).to(exchange).with("messageKey");
        amqpAdmin.declareBinding(binding);
    }

    @PostConstruct
    private void calcBinding() {
        amqpAdmin.declareQueue(calcQ);
        Binding binding = BindingBuilder.bind(calcQ).to(exchange).with("calcKey");
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

    @RabbitListener(queues = "calcQ")
    public double receiveCalc(CalcNumber calcNumber) {
        double num1 = calcNumber.getNum1();
        double num2 = calcNumber.getNum2();
        String operation = calcNumber.getOperation();

        System.out.println("[Request] First Operand: " + num1);
        System.out.println("[Request] Second Operand: " + num2);
        System.out.println("[Request] Operation: " + operation);
        double response = 0;
        if(operation.equalsIgnoreCase("add")) {
            response = num1 + num2;
        } else if (operation.equalsIgnoreCase("sub") || operation.equalsIgnoreCase("minus") || operation.equalsIgnoreCase("subtraction")) {
            response = num1 - num2;
        } else if (operation.equalsIgnoreCase("div") || operation.equalsIgnoreCase("divide")) {
            response = num2 != 0 ? num1/num2 : -0;
        } else if(operation.equalsIgnoreCase("mul") || operation.equalsIgnoreCase("multiply")) {
            response = num1*num2;
        } else {
            System.out.println("Invalid Operation");
        }
        return response;
    }

    @RabbitListener(queues = "converterQ")
    public double receiveCurrency(float nepaliCurrency) {
        System.out.println("[Request] Nepali Currency: " + nepaliCurrency);
        return nepaliCurrency/1.6;
    }
}
