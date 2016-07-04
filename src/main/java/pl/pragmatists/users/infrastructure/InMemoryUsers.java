package pl.pragmatists.users.infrastructure;

import pl.pragmatists.users.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class InMemoryUsers implements Users {

    private final List<User> db;

    public InMemoryUsers(List<User> db) {
        this.db = db;
    }

    @Override
    public Users withEmail(Email email) {
        return new InMemoryUsers(db.stream().filter(u -> u.has(email)).collect(Collectors.toList()));
    }

    @Override
    public int count() {
        return db.size();
    }

    @Override
    public Iterator<User> iterator() {
        return db.iterator();
    }
}
