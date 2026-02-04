package br.ufrn.manageit.config;

import br.ufrn.manageit.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;

    public SecurityConfig(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/index.html", "/cadastro", "/cadastro.html",
                                "/usuarios/cadastrar").permitAll()

                        .requestMatchers("/login", "/login.html", "/error", "/styles/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers("/itens/**").hasRole("ADMIN")
                        .requestMatchers("/movimentacoes/*/cancelar",
                                "/movimentacoes/*/confirmar",
                                "/movimentacoes/*/devolucao").hasRole("ADMIN")

                        .requestMatchers("/movimentacoes/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/pessoas/**",
                                "/processos-seletivos/**",
                                "/setores/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/itens.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout=true")
                        .permitAll()
                )
                .userDetailsService(usuarioService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}