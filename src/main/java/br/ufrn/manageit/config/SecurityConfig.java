package br.ufrn.manageit.config;

import br.ufrn.manageit.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
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
                        .requestMatchers("/login", "/login.html", "/error",
                                "/styles/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/itens/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/itens/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/itens/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/itens/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/movimentacoes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/movimentacoes/**").hasRole("ADMIN")
                        .requestMatchers("/movimentacoes/*/cancelar",
                                "/movimentacoes/*/confirmar",
                                "/movimentacoes/*/devolucao").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pessoas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pessoas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/pessoas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pessoas/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/processos-seletivos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/processos-seletivos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/processos-seletivos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/processos-seletivos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/setores/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/setores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/setores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/setores/**").hasRole("ADMIN")

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