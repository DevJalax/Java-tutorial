@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login", "/resources/**").permitAll() // Allow access to login and static resources
                .anyRequest().authenticated() // All other requests need to be authenticated
            .and()
            .formLogin()
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("https://www.google.com", true) // Redirect to Google after login
                .permitAll()
            .and()
            .sessionManagement()
                .maximumSessions(1) // Only one session per user
                .expiredUrl("/login?expired=true") // Redirect to login if session expires
                .maxSessionsPreventsLogin(true) // Prevents login if session already exists
            .and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); // Session created only if required
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        // Define users and roles
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user1").password("password1").roles("USER").build());
        manager.createUser(users.username("user2").password("password2").roles("USER").build());
        return manager;
    }
}
