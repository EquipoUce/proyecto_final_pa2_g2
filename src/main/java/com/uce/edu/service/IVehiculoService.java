package com.uce.edu.service;

import java.util.List;

import com.uce.edu.repository.modelo.Vehiculo;
import com.uce.edu.repository.modelo.dto.VehiculoDTO;

public interface IVehiculoService {
	public Vehiculo buscarPorPlaca (String placa);
	public List<VehiculoDTO> buscarPorModeloMarca (String marca, String modelo);
	public void guardar (Vehiculo vehiculo);
	public void actualizar (Vehiculo vehiculo);

}
