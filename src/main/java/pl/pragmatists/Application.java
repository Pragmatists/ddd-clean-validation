package pl.pragmatists;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.pragmatists.users.infrastructure.RegisterBoundedContext;

@Configuration
@EnableAutoConfiguration
@Import({RegisterBoundedContext.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
