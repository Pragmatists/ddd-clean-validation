package pl.pragmatists.users.ui;

import pl.pragmatists.infrastructure.concordion.BasicFixture;

import static pl.pragmatists.users.domain.Email.of;

public class HappyPathFixture extends BasicFixture {

    public boolean userExists(String userName) {
        return userRepository.load(of(userName)) != null;
    }
}
