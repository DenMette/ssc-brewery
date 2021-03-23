package guru.sfg.brewery.security;

import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Maarten Casteels
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsJpaService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Getting User information via JPA!");

        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username '%s' not found.", username)));
    }
}
