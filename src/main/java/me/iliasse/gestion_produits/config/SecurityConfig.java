package me.iliasse.gestion_produits.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    CustomAccessDeniedHandlerConfig customAccessDeniedHandler;
    CustomAuthenticationEntryPointConfig customEntryPoint;

    public SecurityConfig(CustomAccessDeniedHandlerConfig customAccessDeniedHandler, CustomAuthenticationEntryPointConfig customEntryPoint){
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customEntryPoint = customEntryPoint;

    }

    @Bean PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){

        PasswordEncoder encoder = this.passwordEncoder();

        return new InMemoryUserDetailsManager(
                User.withUsername("visitor").password(encoder.encode("visitor")).roles("VISITOR").build(),
                User.withUsername("user").password(encoder.encode("user")).roles("USER").build(),
                User.withUsername("admin").password(encoder.encode("admin")).roles("USER", "ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {

        String[] authorized_ressources = {
                "/logout",
                "/login",
                "/products",
                "/webjars/**",
                "/js/**",
                "/images/**",
                "/uploads/**",
                "/error/**",
                "/"
        };

        return http
                .formLogin(lf -> 
                        lf.loginPage("/login").defaultSuccessUrl("/products", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/products")
                        .invalidateHttpSession(true)
                )
                .authorizeHttpRequests(
                        ar -> ar.requestMatchers("/admin/products/new", "/admin/products/save", "/admin/products").hasRole("USER")
                )
                .authorizeHttpRequests(
                        ar -> ar.requestMatchers("/admin/**").hasRole("ADMIN")
                )
                .authorizeHttpRequests(
                        ar -> ar.requestMatchers(authorized_ressources).permitAll()
                )
                .exceptionHandling(eh ->
                    eh.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customEntryPoint)
                )
                .build();
    }
}
