package pl.pragmatists.users.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.pragmatists.infrastructure.rest.RestConfig;
import pl.pragmatists.users.application.UserFacade;
import pl.pragmatists.users.domain.User;
import pl.pragmatists.users.domain.UserFactory;
import pl.pragmatists.users.domain.UserRepository;
import pl.pragmatists.users.domain.Users;
import pl.pragmatists.users.ui.RegisterResource;

import java.util.ArrayList;

@Configuration
@Import(RestConfig.class)
public class RegisterBoundedContext {

    private ArrayList<User> db = new ArrayList<>();

    @Bean
    Users users() {
        return new InMemoryUsers(db);
    }

    @Bean
    UserRepository userRepository(Users users) {
        return new InMemoryUserRepository(db, users);
    }

    @Bean
    UserFactory userFactory(Users users) {
        return new UserFactory(users);
    }

    @Bean
    UserFacade userFacade(UserRepository userRepository, UserFactory factory) {
        return new UserFacade(factory, userRepository);
    }

    @Bean
    RegisterResource registerResource(UserFacade userFacade, Users users) {
        return new RegisterResource(userFacade, users);
    }
}
