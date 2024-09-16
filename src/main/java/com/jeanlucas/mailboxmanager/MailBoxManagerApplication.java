package com.jeanlucas.mailboxmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "MailBoxManager", version = "1", description = "API para gestão de caixas de e-mail, pastas e mensagens"))
public class MailBoxManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailBoxManagerApplication.class, args);
    }

}
