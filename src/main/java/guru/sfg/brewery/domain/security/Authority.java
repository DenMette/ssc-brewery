package guru.sfg.brewery.domain.security;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maarten Casteels
 */
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();
}
