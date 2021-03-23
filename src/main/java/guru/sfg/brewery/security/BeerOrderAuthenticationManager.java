package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Maarten Casteels
 */
@Slf4j
@Component
public class BeerOrderAuthenticationManager {

    public boolean customerIdMatches(Authentication authentication, UUID customerId) {
        User authenticatedUser = (User) authentication.getPrincipal();

        log.debug("Authenticated User CustomerID '{}', Given CustomerID '{}'.", authenticatedUser.getCustomer().getId(), customerId);

        return authenticatedUser.getCustomer().getId().equals(customerId);
    }
}
