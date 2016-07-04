package pl.pragmatists.users.domain;

import pl.pragmatists.validation.ExceptionThrowingListener;
import pl.pragmatists.validation.ValidationListener;

public class User {

    private String id;

    private Password password;

    private PromotionalCode promotionalCode;


    private User(Email login, Password password, PromotionalCode promotionalCode) {
        this.id = login.toString();
        this.password = password;
        this.promotionalCode = promotionalCode;
    }

    public static User newUser(Email login, Password password, PromotionalCode promotionalCode, Users users) {
        User user = new User(login, password, promotionalCode);
        user.validateWith(new EmailUniquenessValidator(users, new ExceptionThrowingListener()));
        return user;
    }

    private void validateWith(EmailUniquenessValidator emailUniquenessValidator) {
        emailUniquenessValidator.validate(email());
    }

    public boolean has(Email email) {
        return email().equals(email);
    }

    public Email email() {
        return Email.of(id);
    }

    public static class EmailUniquenessValidator {

        private final Users users;
        private final ValidationListener listener;

        public EmailUniquenessValidator(Users users, ValidationListener email) {
            this.users = users;
            this.listener = email;
        }

        public void validate(Email email){
            if (users.withEmail(email).count() > 0) {
                listener.reject(new UserAlreadyExists(email));
            }
        }
    }
}
