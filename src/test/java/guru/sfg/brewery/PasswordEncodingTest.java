package guru.sfg.brewery;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @author Maarten Casteels
 */
public class PasswordEncodingTest {

    static final String PASSWORD = "password";
    static final String GURU = "guru";
    static final String TIGER = "tiger";

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();

        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(GURU));
        System.out.println(ldap.encode(TIGER));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();

        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(GURU));
        System.out.println(sha256.encode(TIGER));
    }

    @Test
    void testBCrypt() {
        PasswordEncoder encoder = new BCryptPasswordEncoder(15);

        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode(GURU));
        System.out.println(encoder.encode(TIGER));
    }
}
