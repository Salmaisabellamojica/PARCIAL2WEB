package usco.edu.co.parcial2.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import usco.edu.co.parcial2.model.Usuario;
import usco.edu.co.parcial2.repository.UsuarioRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DatabaseUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .toList();

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!Boolean.TRUE.equals(usuario.getEnabled()))
                .authorities(authorities)
                .build();
    }
}

