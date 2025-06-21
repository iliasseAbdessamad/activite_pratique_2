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

        String[] authorized_ressources = { "/", "/products/**", "/webjars/**", "/js/**", "/images/**", "/uploads/**" };

        return http
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(
                        ar -> ar.requestMatchers("/admin/new", "/admin/save").hasRole("USER")
                )
                .authorizeHttpRequests(
                        ar -> ar.requestMatchers("/admin/**").hasRole("ADMIN")
                )
                .authorizeHttpRequests(
                        ar -> ar.requestMatchers(authorized_ressources).permitAll()
                )
                .build();
    }
}
