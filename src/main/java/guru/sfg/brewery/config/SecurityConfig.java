package guru.sfg.brewery.config;

import guru.sfg.brewery.security.PasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Maarten Casteels
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) ->
                requests
                        .antMatchers("/", "/webjars/**", "/resources/**", "/api/v1/beer").permitAll()
                        .antMatchers("/beers/find", "/beers").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
                        .anyRequest().authenticated());
        http.formLogin();
        http.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                    .withUser("spring")
                    .password("{ldap}{SSHA}sap4pE15vBNxeyUoIoXZJ0NM1fJRn/8aWrCD5A==")
                    .roles("ADMIN")
                .and()
                    .withUser("user")
                    .password("{bcrypt}$2a$10$U.QzyUoIle/rbTjazBsRzuEoKeBhWLVmSLmtrZ7Lfb0acO.57nFHC")
                    .roles("USER")
                .and()
                    .withUser("scott")
                    .password("{sha256}10d75e8f794fbe01cac4220ecfa49acfc8b468e596861c0cd717b2a04ed8aeda730a3d59ff57110d")
                    .roles("CUSTOMER");
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
