package guru.sfg.brewery.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Maarten Casteels
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.update') " +
        "OR (hasAuthority('customer.order.update') " +
        "AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId))")
public @interface OrderBeerUpdatePermission {
}
