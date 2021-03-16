package guru.sfg.brewery;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

/**
 * @author Maarten Casteels
 */
public class PasswordEncondingTest {

    static final String PASSWORD = "password";

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySALTVALUE";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }
}
