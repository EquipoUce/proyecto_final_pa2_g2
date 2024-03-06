package com.uce.edu.controller;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uce.edu.repository.modelo.Cliente;
import com.uce.edu.repository.modelo.Vehiculo;
import com.uce.edu.repository.modelo.dto.ReservaEmpleadoDTO;
import com.uce.edu.service.EmpleadoService;
import com.uce.edu.service.IClienteService;
import com.uce.edu.service.IVehiculoService;
import com.uce.edu.service.TO.ClienteTO;
import com.uce.edu.service.TO.ReservaEmpleadoTO;
import com.uce.edu.service.TO.VehiculoTO;

@Controller
@RequestMapping("/menuEmpleados")
public class MenuEmpleadoController {

	@Autowired
	private IClienteService iClienteService;
	@Autowired
	private IVehiculoService iVehiculoService;

	@Autowired
	private EmpleadoService empleadoService;

	@GetMapping("/mostrarFormularioRegistrarCliente")
	public String mostrarFormularioRegistrarClienteDesdeError(Model model) {
		model.addAttribute("clienteTO", new ClienteTO());
		return "formularioRegistrarCliente3";
	}

	// --> http://localhost:8080/menuEmpleados/mostrarFormularioBuscarCliente?
	@GetMapping("/mostrarFormularioBuscarCliente")
	public String mostrarFormularioBuscarCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("tituloMensaje", MensajesUsuario.ADVERTENCIA.getMensaje());
		model.addAttribute("mensaje", MensajesUsuario.BUCLE.getMensaje());
		return "formularioBuscarCliente9";
	}

	// -->
	@PostMapping("/BuscarClientePorCedula")
	public String BuscarClientePorCedula(@RequestParam("cedula") String cedula, Model model) {
		Cliente cliente = iClienteService.buscarPorCedula(cedula);

		if (cedula.isEmpty()) {

			return "redirect:/menuEmpleados/mostrarFormularioBuscarCliente";
		}

		else if (cliente != null) {
			model.addAttribute("cliente", cliente);
			return "vistaBuscarCliente10";
		} else {
			return "redirect:/menuPrincipal/mostrarMenuEmpleado";
		}
	}

	@GetMapping("/mostrarFormularioRegistrarVehiculo")
	public String mostrarFormularioRegistrarVehiculo(Model model) {
		model.addAttribute("vehiculoTO", new VehiculoTO());
		return "formularioRegistrarVehuculo11";
	}

	@PostMapping("/insertarVehiculo")
	public String insertarVehiculo(@ModelAttribute("vehiculoTO") VehiculoTO vehiculoTO, Model model) {
			this.empleadoService.guardarVehiculo(vehiculoTO);
			return "redirect:/menuPrincipal/mostrarMenuEmpleado";
	}

	@GetMapping("/mostrarFormularioBuscarVehiculoEmpleado")
	public String mostrarFormularioBuscarVehiculo(Model model) {
		model.addAttribute("vehiculo", new Vehiculo());
		model.addAttribute("tituloMensaje", MensajesUsuario.ADVERTENCIA.getMensaje());
		model.addAttribute("mensaje", MensajesUsuario.BUCLE.getMensaje());
		return "formularioBuscarVehiculoPlaca12";
	}

	@PostMapping("/BuscarVehiculoPorPlaca")
	public String BuscarVehiculoPorPlaca(@RequestParam("placa") String placa, Model model) {
		Vehiculo vehiculo = iVehiculoService.buscarPorPlaca(placa);
		if (placa.isEmpty()) {

			return "redirect:/menuEmpleados/mostrarFormularioBuscarVehiculoEmpleado";
		}
		
		if (vehiculo != null) {
			model.addAttribute("vehiculo", vehiculo);
			return "vistaBuscarVehiculo13";
		} else {
			return "redirect:/menuEmpleados/mostrarFormularioBuscarVehiculoEmpleado";
		}
	}

	// --> http://localhost:8080/menuEmpleados/mostrarFormularioBuscarNumeroReserva

	@GetMapping("/mostrarFormularioBuscarNumeroReserva")
	public String mostrarFormularioBuscarNumeroReserva(Model model) {
		model.addAttribute("reservaEmpleadoTO", new ReservaEmpleadoTO());
		model.addAttribute("tituloMensaje", MensajesUsuario.ADVERTENCIA.getMensaje());
		model.addAttribute("mensaje", MensajesUsuario.BUCLE.getMensaje());
		return "formularioNumeroReserva14";
	}

	@PostMapping("/buscarReservaPorNumero")
	public String buscarReservaPorNumero(@RequestParam("numeroReserva") String numeroReserva, Model model) {
		ReservaEmpleadoTO reservaEmpleadoTO = this.empleadoService.generarRservaEmpleadoTO(numeroReserva);
		if (numeroReserva.isEmpty()) {
			return "redirect:/menuEmpleados/mostrarFormularioBuscarNumeroReserva";
		}
		if (reservaEmpleadoTO != null) {
			model.addAttribute("reservaEmpleadoTO", reservaEmpleadoTO);
			return "vistaBuscaReservaNumero15";
		} else {
			return "redirect:/menuEmpleados/mostrarFormularioBuscarNumeroReserva";
		}
	}

	@PutMapping("/actualizarEstadoReserva/{numeroReserva}")
	public String actualizarEstadoReserva(@PathVariable("numeroReserva") String numeroReserva,
		ReservaEmpleadoTO reservaEmpleadoTO, Model model) {
		reservaEmpleadoTO = this.empleadoService.generarRservaEmpleadoTO(numeroReserva);
		if(!reservaEmpleadoTO.getEstado().equals("Ejecutada")) {
		this.empleadoService.ejecutarReserva(reservaEmpleadoTO);
		String mensajeExito = "La reserva: " + reservaEmpleadoTO.getNumeroReserva() + " se actualizo con éxito";
		model.addAttribute("mensajeExito", mensajeExito);
		return "mensajeTransaccionExitosa";
		}
		else {
			String mensajeFracaso = "Error";
			String mensaje = "La reserva: " + reservaEmpleadoTO.getNumeroReserva() + " ya esta ejecutada, no puede cambiar su estado nuevamente";
			model.addAttribute("tituloMensaje", mensajeFracaso);
			model.addAttribute("mensaje", mensaje);
			return "errorReservaYaEjecutada";
		}
	}

	@GetMapping("/mostrarRetirarSinReserva")
	public String mostrarRetirarSinReserva() {
		// Utilizamos "redirect:" para indicar la redirección
		return "menuSinReserva16";
	}

}
