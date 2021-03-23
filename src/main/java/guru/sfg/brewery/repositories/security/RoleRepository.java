package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Maarten Casteels
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
