package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Maarten Casteels
 */
@RequiredArgsConstructor
@Service
public class UserDetailsJpaService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username '%s' not found.", username)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                convertToAuthorities(user.getAuthorities())
        );
    }

    private Collection<? extends GrantedAuthority> convertToAuthorities(Set<Authority> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return new HashSet<>();
        }

        return authorities.stream()
                .map(Authority::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
