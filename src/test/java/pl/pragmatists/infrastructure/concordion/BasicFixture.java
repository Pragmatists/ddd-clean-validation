package pl.pragmatists.infrastructure.concordion;

import org.springframework.beans.factory.annotation.Autowired;
import pl.pragmatists.users.domain.UserRepository;

public abstract class BasicFixture extends RestFixture {
    @Autowired
    protected UserRepository userRepository;

    public void clearUsers() {
        userRepository.deleteAll();
    }
}
