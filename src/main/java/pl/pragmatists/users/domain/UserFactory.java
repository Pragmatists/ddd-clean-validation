package pl.pragmatists.users.domain;

public class UserFactory {

    private final Users users;

    public UserFactory(Users users) {
        this.users = users;
    }

    public User create(Email login, Password password, PromotionalCode promotionalCode) {
        return User.newUser(login, password, promotionalCode, users);
    }
}
