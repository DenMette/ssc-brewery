package guru.sfg.brewery.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Maarten Casteels
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.read') " +
        "OR (hasAuthority('customer.order.read') " +
        "AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId))")
public @interface OrderBeerReadPermission {
}
