package pl.pragmatists.users.ui;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pragmatists.users.application.UserFacade;
import pl.pragmatists.users.domain.Email;
import pl.pragmatists.users.domain.User;
import pl.pragmatists.validation.Pair;
import pl.pragmatists.users.domain.Password;
import pl.pragmatists.users.domain.PromotionalCode;
import pl.pragmatists.users.domain.Users;
import pl.pragmatists.validation.AccumulatingValidationListener;
import pl.pragmatists.validation.ValidationChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.pragmatists.validation.Pair.of;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterResource {

    private final UserFacade application;
    private final Users users;

    public RegisterResource(UserFacade application, Users users) {
        this.application = application;
        this.users = users;
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public void register(
        @RequestBody NewUserRequest json,
        @RequestParam(value = "dryRun", defaultValue = "false") boolean dryRun) {
        Consumer<NewUserRequest> action = dryRun ? this::doNothing : this::doRegister;
        register(json, action);
    }

    public void register(NewUserRequest json, Consumer<NewUserRequest> action) {

        AccumulatingValidationListener errors = AccumulatingValidationListener.create();

        Email.Validator emailValidator = new Email.Validator(errors.forPath("login"));
        RequiredValidator loginRequired = new RequiredValidator(errors.forPath("login"));
        User.EmailUniquenessValidator unique = new User.EmailUniquenessValidator(users, errors.forPath("login"));
        ValidationChain.of(String.class)
            .check(loginRequired::validate)
            .check(emailValidator::validate)
            .then(Email::of)
            .check(unique::validate)
            .test(json.login);

        RequiredValidator pwdRequired = new RequiredValidator(errors.forPath("password"));
        Password.Validator strongPassword = new Password.Validator(errors.forPath("password"));
        ValidationChain.of(String.class)
            .check(pwdRequired::validate)
            .check(strongPassword::validate)
            .test(json.password);

        IdenticalValidator identicalPassword = new IdenticalValidator(errors.forPath("passwordAgain"));
        ValidationChain.of(Pair.class)
            .check(identicalPassword::validate)
            .test(of(json.password, json.passwordAgain));

        PromotionalCode.Validator promotionPolicyAccepted = new PromotionalCode.Validator(errors.forPath("promotionPolicyAccepted"));
        ValidationChain.of(Pair.class)
            .check(promotionPolicyAccepted::validate)
            .test(of(json.coupon, json.promotionPolicyAccepted));

        List<AccumulatingValidationListener.Error> problems = errors.list();
        if (!problems.isEmpty()) {
            throw new BadRequest(problems);
        }

        action.accept(json);
    }

    private static ErrorsResponse asErrors(Collection<AccumulatingValidationListener.Error> errors) {
        ErrorsResponse errorsResponse = new ErrorsResponse();
        for (AccumulatingValidationListener.Error error : errors) {
            errorsResponse.add(error.path(), error.message());
        }
        return errorsResponse;
    }

    private void doRegister(NewUserRequest json) {
        application.register(
            Email.of(json.login),
            Password.of(json.password),
            PromotionalCode.of(json.coupon, json.promotionPolicyAccepted));
    }

    public <T> void doNothing(T json) {
    }

    static class NewUserRequest {
        String login;
        String password;
        String passwordAgain;
        String coupon;
        boolean promotionPolicyAccepted;
    }

    static class ErrorsResponse {
        Collection<ErrorJson> errors = new ArrayList<>();

        void add(String path, String message) {
            errors.add(new ErrorJson(path, message));
        }
    }

    static class ErrorJson {
        private String path;
        private String message;

        private ErrorJson(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public static ErrorJson error(String path, String message) {
            return new ErrorJson(path, message);
        }
    }

    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handle(BadRequest badRequest) {
        return asErrors(badRequest.errors());
    }
}


