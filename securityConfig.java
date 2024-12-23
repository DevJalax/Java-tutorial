import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure security rules
        http
            .authorizeRequests()
                .antMatchers("/", "/login", "/error").permitAll() // Allow access to these URLs
                .anyRequest().authenticated() // Protect all other URLs
            .and()
            .oauth2Login()
                .defaultSuccessUrl("/home", true) // Redirect to /home after successful login
            .and()
            .sessionManagement()
                .maximumSessions(1) // Limit to single session
                .expiredUrl("/login?expired") // Redirect if the session expires
                .and()
                .sessionFixation().migrateSession() // Protect against session fixation
            .and()
            .csrf().and() // CSRF protection
            .headers()
                .contentSecurityPolicy("script-src 'self'; object-src 'none';")
                .and()
                .frameOptions().deny() // Clickjacking protection
            .and()
            .addFilterBefore(new CharacterEncodingFilter(), CsrfFilter.class); // Prevent XSS attacks

        // Add security filter configuration here if needed
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Define a sample user for RBAC (Role-Based Access Control)
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("adminpassword"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher(); // Enables session management
    }
}
