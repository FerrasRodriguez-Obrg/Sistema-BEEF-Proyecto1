// src/main/java/edu/tecnm/security/DatabaseWebSecurity.java

package edu.tecnm.security; 

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        
        // Login: Únicamente por 'username'
        users.setUsersByUsernameQuery(
            "SELECT username, password, estatus FROM usuarios u WHERE username = ?"
        );

        // 🛑 CORRECCIÓN FINAL: Devolver el perfil/rol siempre en MAYÚSCULAS (UPPER())
        users.setAuthoritiesByUsernameQuery(
            "SELECT u.username, UPPER(p.perfil) " 
            + "FROM usuario_perfil up " 
            + "INNER JOIN usuarios u ON u.id = up.idUsuario " 
            + "INNER JOIN perfiles p ON p.id = up.idPerfil " 
            + "WHERE u.username = ?"
        );
        
        return users;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard.html", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            
         // ...
         // Reglas de Autorización (Ahora siempre en MAYÚSCULAS)
         .authorizeHttpRequests(authorize -> authorize
             
             // --- ACCESO PÚBLICO (Incluye Menú y Carrito/Pedido) ---
             .requestMatchers(
                 "/", 
                 "/login", 
                 "/logout", 
                 "/css/**", "/js/**", "/images/**",
                 "/menu/cliente", // Ya existía, mantenemos por si acaso
                 
                 // 🛑 CRÍTICO: Agregar las rutas del Menú Cliente y el Carrito
                 "/menucliente", 
                 "/pedido/agregar/**", 
                 "/pedido/carrito",
                 
                 "/invitado/**"
             ).permitAll()
             
             // --- SUPERVISOR / ADMINISTRADOR (Gestión de Recursos) ---
             .requestMatchers(
                 "/usuarios/**",  
                 "/perfiles/**",
                 "/productos/**", 
                 "/mesas/**", 
                 
                 // 🛑 AGREGADO: Regla para empleados (Supervisor)
                 "/empleados/**"
             ).hasAnyAuthority("SUPERVISOR", "ADMINISTRADOR")
             
             // --- CAJERO (Ventas, Clientes, Reservación) ---
             .requestMatchers(
                 "/clientes/**",          
                 "/ventas/crear",       
                 "/reservaciones/lista",
                 
                 // 🛑 AGREGADO: Permiso para crear Reservaciones
                 "/reservaciones/crear" 
             ).hasAnyAuthority("CAJERO", "ADMINISTRADOR")
             
             // ... (El resto de las reglas se mantienen iguales) ...
             
        

                .anyRequest().authenticated()
            )
            
            .exceptionHandling(exceptions -> exceptions
                 .accessDeniedPage("/acceso-denegado")
            );
            
        return http.build();
    }
}