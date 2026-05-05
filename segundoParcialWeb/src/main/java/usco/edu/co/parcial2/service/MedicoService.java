package usco.edu.co.parcial2.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usco.edu.co.parcial2.dto.MedicoRequest;
import usco.edu.co.parcial2.model.Medico;
import usco.edu.co.parcial2.model.Rol;
import usco.edu.co.parcial2.model.Usuario;
import usco.edu.co.parcial2.repository.MedicoRepository;
import usco.edu.co.parcial2.repository.RolRepository;
import usco.edu.co.parcial2.repository.UsuarioRepository;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(MedicoRepository medicoRepository, RolRepository rolRepository,
            UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    @Transactional
    public Medico crear(MedicoRequest request) {
        Rol rolMedico = rolRepository.findByNombre("MEDICO")
                .orElseThrow(() -> new IllegalStateException("Rol MEDICO no existe"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.getRoles().add(rolMedico);
        usuario = usuarioRepository.save(usuario);

        Medico medico = new Medico();
        medico.setNombre(request.nombre());
        medico.setEspecialidad(request.especialidad());
        medico.setDocumento(request.documento());
        medico.setUsuario(usuario);
        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico actualizar(Long id, MedicoRequest request) {
        Medico medico = buscar(id);
        medico.setNombre(request.nombre());
        medico.setEspecialidad(request.especialidad());
        medico.setDocumento(request.documento());
        medico.getUsuario().setUsername(request.username());
        if (!request.password().isBlank()) {
            medico.getUsuario().setPassword(passwordEncoder.encode(request.password()));
        }
        return medico;
    }

    @Transactional
    public void eliminar(Long id) {
        Medico medico = buscar(id);
        medicoRepository.delete(medico);
        if (medico.getUsuario() != null) {
            usuarioRepository.delete(medico.getUsuario());
        }
    }

    public Medico buscar(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
    }
}

