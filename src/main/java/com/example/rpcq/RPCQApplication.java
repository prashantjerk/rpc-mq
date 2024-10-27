package com.example.rpcq;

import com.example.rpcq.rpcPattern.RPCClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class RPCQApplication {
	@Profile("client")
	@Bean
	public CommandLineRunner execute(RPCClient client) {
		return args -> {
			client.send();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(RPCQApplication.class, args);
	}

}
