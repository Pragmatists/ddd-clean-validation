package pl.pragmatists.users.infrastructure;

import pl.pragmatists.validation.ExceptionThrowingListener;
import pl.pragmatists.users.domain.*;

import java.util.List;

class InMemoryUserRepository implements UserRepository {

    private final List<User> db;

    private final Users users;

    protected InMemoryUserRepository(List<User> db, Users users) {
        this.db = db;
        this.users = users;
    }

    @Override
    public void store(User user) {
        new User.EmailUniquenessValidator(users, new ExceptionThrowingListener())
            .validate(user.email());
        db.add(user);
    }

    @Override
    public User load(Email email) {
        return db.stream()
            .findFirst()
            .filter(user -> user.has(email))
            .orElseThrow(() -> new UserNotFound(email));
    }

    @Override
    public void deleteAll() {
        db.clear();
    }
}

