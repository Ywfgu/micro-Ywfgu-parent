package com.ltyc;

import com.ltyc.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaseNettyApplication implements CommandLineRunner {
    @Autowired
    private NettyClient nettyClient;

	public static void main(String[] args) {
		SpringApplication.run(BaseNettyApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        nettyClient.run();
    }
}
