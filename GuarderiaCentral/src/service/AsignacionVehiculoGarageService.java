package service;

import model.AsignacionVehiculoGarage;
import model.Vehiculo;
import model.Garage;
import model.Zona;
import dto.AsignacionVehiculoGarageDTO;
import mapper.AsignacionVehiculoGarageMapper;
import dao.AsignacionVehiculoGarageDAO;
import dao.impl.AsignacionVehiculoGarageDAOImpl;
import dto.GarageDTO;
import dto.VehiculoDTO;
import exceptions.ErrorNegocio;
import exceptions.ZonaSinCapacidadException;
import exceptions.RegistroNoEncontradoException; // Import necesario
import java.util.List;
import mapper.GarageMapper;
import mapper.VehiculoMapper;

public class AsignacionVehiculoGarageService {

    private AsignacionVehiculoGarageDAO dao;
    // Necesitamos los servicios de Vehiculo y Garage para buscar los objetos completos
    

    public AsignacionVehiculoGarageService() {
        this.dao = new AsignacionVehiculoGarageDAOImpl();
        
    }

    public void crearAsignacion(AsignacionVehiculoGarageDTO dto) throws ErrorNegocio {
        VehiculoService vehiculoService= new VehiculoService();
        GarageService garageService= new GarageService();
        
        // 1. Validaciones básicas de integridad (nivel DTO)
        if (dto == null) {
            throw new ErrorNegocio("Error: El objeto de asignación no puede ser nulo.");
        }
        if (dto.getVehiculo() == null || dto.getVehiculo().getId() <= 0) {
            throw new ErrorNegocio("Error: Se requiere un vehículo válido para la asignación.");
        }
        if (dto.getGarage() == null || dto.getGarage().getId() <= 0) {
            throw new ErrorNegocio("Error: Se requiere un garaje válido para la asignación.");
        }
        if (dto.getFechaAsignacionGarage() == null) {
            throw new ErrorNegocio("Error: La fecha de asignación es obligatoria.");
        }

        // 2. RECUPERAR OBJETOS COMPLETOS DE LA BASE DE DATOS (Lógica del Servicio)
        Vehiculo vehiculoCompleto;
        Garage garageCompleto; // Si GarageService retorna DTO, harías lo mismo
        try {
            // Convertimos el DTO retornado por el servicio a Modelo usando el Mapper
            VehiculoDTO vDto = vehiculoService.buscarPorId(dto.getVehiculo().getId());
            vehiculoCompleto = VehiculoMapper.toModel(vDto);

            // Lo mismo para Garage si GarageService retorna DTO
            garageCompleto = garageService.buscarPorId(dto.getGarage().getId());
        } catch (RegistroNoEncontradoException e) {
            throw new ErrorNegocio("Error al buscar vehículo o garaje: " + e.getMessage());
        }

        // 3. MApeo: Ahora convertimos el DTO a Modelo (pasando los objetos completos)
        // IMPORTANTE: El mapper debe estar actualizado para recibir esta estructura
        AsignacionVehiculoGarage nuevaAsignacion = AsignacionVehiculoGarageMapper.toModel(dto, garageCompleto, vehiculoCompleto);

        // 4. Validación de Regla de Negocio: Compatibilidad de Tipo de Vehículo
        if (vehiculoCompleto.getTipo() != garageCompleto.getZona().getTipoVehiculo()) {
            throw new ErrorNegocio("Error: El vehículo de tipo " + vehiculoCompleto.getTipo()
                    + " no puede ser asignado en una zona destinada a " + garageCompleto.getZona().getTipoVehiculo() + ".");
        }

        // 5. Validación de Regla de Negocio: Fechas
        if (garageCompleto.getFechaCompra() != null) {
            if (nuevaAsignacion.getFechaAsignacionGarage().isBefore(garageCompleto.getFechaCompra())) {
                throw new ErrorNegocio("Error: La fecha de asignación no puede ser anterior a la fecha de compra del garaje (" + garageCompleto.getFechaCompra() + ").");
            }
        }

        // 6. Validación de Capacidad de la Zona
        Zona zona = garageCompleto.getZona();
        int capacidadMaxima = zona.getCapacidadVehiculos();
        int vehiculosActuales = contarVehiculosActivosEnZona(zona);

        if (vehiculosActuales >= capacidadMaxima) {
            throw new ZonaSinCapacidadException("Error: La zona '" + zona.getLetra()
                    + "' ha alcanzado su capacidad máxima de " + capacidadMaxima + " vehículos.");
        }

        // 7. Persistir el objeto Modelo
        dao.guardar(nuevaAsignacion);
    }

    private int contarVehiculosActivosEnZona(Zona zona) {
        List<AsignacionVehiculoGarage> todas = dao.listarTodas();
        int contador = 0;
        for (AsignacionVehiculoGarage asignacion : todas) {
            // Verificamos que la asignación no esté dada de baja (si tuviera ese campo)
            // Y que el garage pertenezca a la zona buscada
            if (asignacion.getGarage() != null && asignacion.getGarage().getZona() != null) {
                if (asignacion.getGarage().getZona().getId() == zona.getId()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public AsignacionVehiculoGarage buscarPorGarage(Garage garage) {
        return dao.buscarPorGarage(garage);
    }

    public List<AsignacionVehiculoGarage> listarTodas() {
        return dao.listarTodas();
    }
    
    public AsignacionVehiculoGarage buscarPorVehiculo(int vehiculoId) {
    // Filtrar en el DAO de asignaciones por el ID del vehículo
    return dao.listarTodas().stream()
              .filter(a -> a.getVehiculo().getId() == vehiculoId)
              .findFirst()
              .orElse(null);
}
    
    
}