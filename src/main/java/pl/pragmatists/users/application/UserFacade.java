package pl.pragmatists.users.application;

import pl.pragmatists.users.domain.Email;
import pl.pragmatists.users.domain.Password;
import pl.pragmatists.users.domain.PromotionalCode;
import pl.pragmatists.users.domain.User;
import pl.pragmatists.users.domain.UserFactory;
import pl.pragmatists.users.domain.UserRepository;

public class UserFacade {

    private final UserFactory factory;
    private final UserRepository userRepository;

    public UserFacade(UserFactory factory, UserRepository userRepository) {
        this.factory = factory;
        this.userRepository = userRepository;
    }

    public void register(Email email, Password password, PromotionalCode promotionalCode) {
        User created = factory.create(email, password, promotionalCode);
        userRepository.store(created);
    }

}
