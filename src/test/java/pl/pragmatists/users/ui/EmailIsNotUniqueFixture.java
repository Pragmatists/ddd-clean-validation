package pl.pragmatists.users.ui;

import org.springframework.beans.factory.annotation.Autowired;
import pl.pragmatists.infrastructure.concordion.BasicFixture;
import pl.pragmatists.users.domain.Email;
import pl.pragmatists.users.domain.Password;
import pl.pragmatists.users.domain.PromotionalCode;
import pl.pragmatists.users.domain.User;
import pl.pragmatists.users.domain.Users;

public class EmailIsNotUniqueFixture extends BasicFixture {

    @Autowired
    private Users users;

    public void createUserWithEmail(String email) {
        userRepository.store(User.newUser(Email.of(email), Password.of("jasdlkafJjda!@211"), PromotionalCode.of("", false), users));
    }
}
