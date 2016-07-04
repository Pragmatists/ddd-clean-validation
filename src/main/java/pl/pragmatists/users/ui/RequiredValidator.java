package pl.pragmatists.users.ui;

import org.apache.commons.lang3.StringUtils;
import pl.pragmatists.users.domain.Required;
import pl.pragmatists.validation.ValidationListener;

class RequiredValidator {

    private final ValidationListener result;

    public RequiredValidator(ValidationListener result) {
        this.result = result;
    }

    public void validate(String check) {
        if (StringUtils.isBlank(check)) {
            result.reject(new Required());
        }
    }

    public void validate(Object check) {
        if (check == null) {
            result.reject(new Required());
        }
    }
}