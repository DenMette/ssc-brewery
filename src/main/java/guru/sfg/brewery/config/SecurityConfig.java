package guru.sfg.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Maarten Casteels
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) ->
                requests
                        .antMatchers("/", "/webjars/**", "/resources/**", "/api/v1/beer").permitAll()
                        .antMatchers("/beers/find", "/beers").permitAll()
                        .antMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll()
                        .anyRequest().authenticated());
        http.formLogin();
        http.httpBasic();
    }
}
