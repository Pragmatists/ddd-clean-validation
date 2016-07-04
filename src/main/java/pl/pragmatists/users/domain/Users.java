package pl.pragmatists.users.domain;

public interface Users extends Iterable<User>{

    Users withEmail(Email email);

    int count();
}
