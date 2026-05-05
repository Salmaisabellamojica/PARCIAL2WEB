package usco.edu.co.parcial2.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import usco.edu.co.parcial2.repository.ConsultorioRepository;
import usco.edu.co.parcial2.service.ConsultaService;
import usco.edu.co.parcial2.service.MedicoService;

@Controller
public class ViewController {

    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final ConsultorioRepository consultorioRepository;

    public ViewController(ConsultaService consultaService, MedicoService medicoService,
            ConsultorioRepository consultorioRepository) {
        this.consultaService = consultaService;
        this.medicoService = medicoService;
        this.consultorioRepository = consultorioRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/sin-permiso")
    public String sinPermiso() {
        return "sin-permiso";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("nombreUsuario", principal.getName());
        model.addAttribute("consultas", consultaService.listarTodas());
        model.addAttribute("medicos", medicoService.listar());
        model.addAttribute("consultorios", consultorioRepository.findAll());
        return "admin";
    }

    @GetMapping("/medico")
    public String medico(Model model, Principal principal) {
        model.addAttribute("nombreUsuario", principal.getName());
        model.addAttribute("consultas", consultaService.listarPorMedico(principal.getName()));
        return "medico";
    }

    @GetMapping("/paciente")
    public String paciente(Model model, Principal principal) {
        model.addAttribute("nombreUsuario", principal.getName());
        model.addAttribute("consultas", consultaService.listarPorPaciente(principal.getName()));
        return "paciente";
    }
}

