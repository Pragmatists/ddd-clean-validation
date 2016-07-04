package pl.pragmatists.users.domain;

public interface UserRepository {

    void store(User user);

    User load(Email email);

    void deleteAll();
}
