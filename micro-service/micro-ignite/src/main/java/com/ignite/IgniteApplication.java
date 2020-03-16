package com.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableIgniteRepositories
public class IgniteApplication implements CommandLineRunner {
    @Autowired
    private ServiceInit init;

	public static void main(String[] args) {
		SpringApplication.run(IgniteApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        init.start();
    }
}
