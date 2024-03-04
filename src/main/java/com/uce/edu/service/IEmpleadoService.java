package com.uce.edu.service;

import com.uce.edu.repository.modelo.Cliente;
import com.uce.edu.repository.modelo.Vehiculo;
import com.uce.edu.repository.modelo.dto.ReservaEmpleadoDTO;
import com.uce.edu.service.TO.ClienteTO;
import com.uce.edu.service.TO.VehiculoTO;

public interface IEmpleadoService {
	public void guardarCliente(ClienteTO cliente);

	public Cliente buscarPorCedula(String cedula);

	public void guardarVehiculo(VehiculoTO vehiculo);

	public Vehiculo buscarPorPlaca(String placa);

	public ReservaEmpleadoDTO retirarReservado(String numeroReserva);

}