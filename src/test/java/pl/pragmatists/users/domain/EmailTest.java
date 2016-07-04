package pl.pragmatists.users.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class EmailTest {

    @Test
    public void disallowCreateInvalidEmail() throws Exception {
        try {
            Email.of("invalidEmail");
            failBecauseExceptionWasNotThrown(InvalidEmail.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidEmail.class);
        }
    }

    @Test
    public void disallowCreateNullEmail() throws Exception {
        try {
            Email.of(null);
            failBecauseExceptionWasNotThrown(InvalidEmail.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidEmail.class);
        }
    }
}