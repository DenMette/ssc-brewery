package guru.sfg.brewery.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Maarten Casteels
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.create') " +
        "OR (hasAuthority('customer.order.create') " +
        "AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId))")
public @interface OrderBeerCreatePermission {
}
