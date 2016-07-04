package pl.pragmatists.users.ui;

import pl.pragmatists.validation.AccumulatingValidationListener;

import java.util.Collection;

class BadRequest extends RuntimeException{
    private final Collection<AccumulatingValidationListener.Error> errors;

    public BadRequest(Collection<AccumulatingValidationListener.Error> errors) {
        this.errors = errors;
    }

    public Collection<AccumulatingValidationListener.Error> errors() {
        return errors;
    }
}
