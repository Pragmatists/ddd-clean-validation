package pl.pragmatists.users.domain;

import org.apache.commons.lang3.StringUtils;
import pl.pragmatists.validation.ExceptionThrowingListener;
import pl.pragmatists.validation.Pair;
import pl.pragmatists.validation.ValidationListener;

public class PromotionalCode {

    private final String value;
    private final boolean accepted;

    private final Validator validator = new Validator(new ExceptionThrowingListener());

    private PromotionalCode(String value, boolean accepted) {
        this.value = value;
        this.accepted = accepted;
    }

    public static PromotionalCode of(String value, boolean policyAccepted) {
        PromotionalCode coupon = new PromotionalCode(value, policyAccepted);
        coupon.validate();
        return coupon;
    }

    private void validate() {
        validator.validate(Pair.of(value, accepted));
    }

    public static class Validator {
        private final ValidationListener validationListener;

        public Validator(ValidationListener validationListener) {
            this.validationListener = validationListener;
        }

        public void validate(Pair<String, Boolean> pair) {
            if (StringUtils.isNoneBlank(pair.param1()) ) {
                if (!pair.param2()) {
                    validationListener.reject(new PolicyAcceptanceRequired());
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PromotionalCode)) {
            return false;
        }
        PromotionalCode id = (PromotionalCode) obj;

        return id.value.equals(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
