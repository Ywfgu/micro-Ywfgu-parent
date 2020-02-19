package com.ltyc.sms;

import com.ltyc.sms.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/17
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private ServiceInit init;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        ApplicationContext ctx = application.run(args);
        SpringUtil.setApplicationContext(ctx);
    }
    @Override
    public void run(String... args) throws Exception {
        init.start();
    }




}
