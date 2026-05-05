package usco.edu.co.parcial2.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usco.edu.co.parcial2.dto.ConsultaRequest;
import usco.edu.co.parcial2.dto.HorarioRequest;
import usco.edu.co.parcial2.model.Consulta;
import usco.edu.co.parcial2.model.Consultorio;
import usco.edu.co.parcial2.model.Medico;
import usco.edu.co.parcial2.model.Paciente;
import usco.edu.co.parcial2.repository.ConsultaRepository;
import usco.edu.co.parcial2.repository.ConsultorioRepository;
import usco.edu.co.parcial2.repository.MedicoRepository;
import usco.edu.co.parcial2.repository.PacienteRepository;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultorioRepository consultorioRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository, ConsultorioRepository consultorioRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultorioRepository = consultorioRepository;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> listarPorMedico(String username) {
        return consultaRepository.findByMedicoUsuarioUsernameOrderByFechaAscHoraInicioAsc(username);
    }

    public List<Consulta> listarPorPaciente(String username) {
        return consultaRepository.findByPacienteUsuarioUsernameOrderByFechaAscHoraInicioAsc(username);
    }

    @Transactional
    public Consulta crear(ConsultaRequest request) {
        Medico medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
        Consultorio consultorio = consultorioRepository.findByNumero(Integer.valueOf(request.numeroConsultorio()))
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado. Seleccione un consultorio registrado."));
        Paciente paciente = pacienteRepository.findByNombreIgnoreCase(request.nombrePaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado. Registre el paciente antes de crear la consulta."));

        validarHorario(request.horaInicio(), request.horaFin());
        validarDisponible(request, medico.getId(), consultorio.getId(), null);

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMotivo(request.motivo());
        consulta.setFecha(request.fecha());
        consulta.setHoraInicio(request.horaInicio());
        consulta.setHoraFin(request.horaFin());
        consulta.setMedico(medico);
        consulta.setConsultorio(consultorio);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta actualizar(Long id, ConsultaRequest request) {
        Consulta consulta = buscar(id);
        Medico medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
        Consultorio consultorio = consultorioRepository.findByNumero(Integer.valueOf(request.numeroConsultorio()))
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado"));
        Paciente paciente = pacienteRepository.findByNombreIgnoreCase(request.nombrePaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        validarHorario(request.horaInicio(), request.horaFin());
        validarDisponible(request, medico.getId(), consultorio.getId(), id);

        consulta.setPaciente(paciente);
        consulta.setMotivo(request.motivo());
        consulta.setFecha(request.fecha());
        consulta.setHoraInicio(request.horaInicio());
        consulta.setHoraFin(request.horaFin());
        consulta.setMedico(medico);
        consulta.setConsultorio(consultorio);
        return consulta;
    }

    @Transactional
    public Consulta actualizarHorarioPaciente(Long id, HorarioRequest request, String username) {
        Consulta consulta = buscar(id);
        if (!consulta.getPaciente().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("Solo puede actualizar citas asignadas a usted");
        }
        validarHorario(request.horaInicio(), request.horaFin());
        boolean ocupado = consultaRepository.existeCruce(request.fecha(), request.horaInicio(), request.horaFin(),
                consulta.getConsultorio().getId(), consulta.getMedico().getId(), id);
        if (ocupado) {
            throw new IllegalArgumentException("El horario ya esta ocupado para ese consultorio o medico");
        }
        consulta.setFecha(request.fecha());
        consulta.setHoraInicio(request.horaInicio());
        consulta.setHoraFin(request.horaFin());
        return consulta;
    }

    @Transactional
    public void eliminar(Long id) {
        consultaRepository.delete(buscar(id));
    }

    private Consulta buscar(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada"));
    }

    private void validarDisponible(ConsultaRequest request, Long medicoId, Long consultorioId, Long ignorarId) {
        boolean ocupado = consultaRepository.existeCruce(request.fecha(), request.horaInicio(), request.horaFin(),
                consultorioId, medicoId, ignorarId);
        if (ocupado) {
            throw new IllegalArgumentException("Ya esta ocupado: existe una consulta en ese dia, horario y consultorio, o el medico ya tiene otra consulta.");
        }
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de finalizacion");
        }
    }
}
