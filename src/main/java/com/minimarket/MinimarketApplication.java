package com.minimarket;

import com.minimarket.entity.Usuario;
import com.minimarket.entity.Rol;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MinimarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinimarketApplication.class, args);
    }

    // Inicializador técnico nativo de datos de seguridad
    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Crear e insertar roles si no existen
            Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN").orElseGet(() -> {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombre("ROLE_ADMIN");
                return rolRepository.save(nuevoRol);
            });

            // 2. Crear el usuario de prueba asegurando la encriptación correcta por framework
            if (!usuarioRepository.findByUsername("cristian").isPresent()) {
                Usuario usuario = new Usuario();
                usuario.setUsername("cristian");
                
                // Encriptación directa en caliente con el bean del sistema
                usuario.setPassword(passwordEncoder.encode("admin123"));
                
                Set<Rol> roles = new HashSet<>();
                roles.add(adminRol);
                usuario.setRoles(roles);
                
                usuarioRepository.save(usuario);
                System.out.println(">>> [INFO] Usuario 'cristian' creado exitosamente con contraseña 'admin123' encriptada.");
            }
        };
    }
}