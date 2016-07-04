package pl.pragmatists.users.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.pragmatists.users.infrastructure.RegisterBoundedContext;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.failBecauseExceptionWasNotThrown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RegisterBoundedContext.class)
public class UserFactoryTest {

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Users users;

    @Test
    public void shouldNotCreateUserWithInvalidEmail() {
        try {
            userFactory.create(Email.of("john"), Password.of("123456789"), PromotionalCode.of("", false));
            failBecauseExceptionWasNotThrown(InvalidEmail.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidEmail.class);
        }
    }

    @Test
    public void shouldNotCreateUserWithSameEmail() {
        createUserWithEmail(Email.of("john.smith@acme.com"));
        try {
            userFactory.create(Email.of("john.smith@acme.com"), Password.of("123456789"), PromotionalCode.of("", false));
            failBecauseExceptionWasNotThrown(UserAlreadyExists.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UserAlreadyExists.class);
        }
    }

    private void createUserWithEmail(Email email) {
        userRepository.store(User.newUser(email, Password.of("1234567890"), PromotionalCode.of("", false), users));
    }
}
