package guru.sfg.brewery.config;

import guru.sfg.brewery.security.PasswordEncoderFactories;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Maarten Casteels
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        // Disable it at the moment
        http.csrf().disable();

        http.authorizeRequests((requests) ->
                requests
                        // Database, shouldn't be used in PRD
                        .antMatchers("/h2-console/**").permitAll()
                        // Some stuff for styling purpose
                        .antMatchers("/", "/webjars/**", "/resources/**").permitAll()
                        .anyRequest().authenticated());
        http.formLogin(loginConfigurer -> {
            loginConfigurer
                    .loginProcessingUrl("/login")
                    .loginPage("/")
                    .successForwardUrl("/")
                    .defaultSuccessUrl("/")
                    .failureUrl("/?error")
            ;
        })
        .logout(logoutConfigurer -> {
            logoutConfigurer
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.GET.name()))
                    .logoutSuccessUrl("/?logout")
                    .permitAll();
        });
        http.httpBasic();

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                    .withUser("spring")
//                    .password("{ldap}{SSHA}sap4pE15vBNxeyUoIoXZJ0NM1fJRn/8aWrCD5A==")
//                    .roles("ADMIN")
//                .and()
//                    .withUser("user")
//                    .password("{bcrypt}$2a$10$U.QzyUoIle/rbTjazBsRzuEoKeBhWLVmSLmtrZ7Lfb0acO.57nFHC")
//                    .roles("USER")
//                .and()
//                    .withUser("scott")
//                    .password("{bcrypt15}$2a$15$npxMGdKtoqkUpD/tj9E9wuLOGavlKeqPDq2x3o6IBX0rSHmh8vvX6")
//                    .roles("CUSTOMER");
//    }

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
