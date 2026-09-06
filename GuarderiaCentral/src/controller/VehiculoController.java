package controller;

import dto.VehiculoDTO;
import exceptions.ErrorNegocio;
import exceptions.MatriculaDuplicadaException;
import service.VehiculoService;
import service.AsignacionVehiculoGarageService;
import model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class VehiculoController implements Controlador {

    private final VehiculoService vehiculoService;
    private final AsignacionVehiculoGarageService asignacionVehiculoGarageService;
    private final Usuario usuarioActual;

    public VehiculoController(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo para inicializar el controlador.");
        }
        this.usuarioActual = usuario;
        this.vehiculoService = new VehiculoService();
        this.asignacionVehiculoGarageService = new AsignacionVehiculoGarageService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        throw new UnsupportedOperationException("El controlador de vehículos no soporta operaciones de inicio de sesión.");
    }

    public List<VehiculoDTO> listarTodosLosVehiculos() {
        return vehiculoService.listarTodos();
    }

    public VehiculoDTO buscarVehiculoPorId(int id) {
        try {
            return vehiculoService.buscarPorId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public void registrarVehiculo(VehiculoDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("El DTO del vehículo no puede ser nulo.");
        }
        try {
            List<VehiculoDTO> existentes = vehiculoService.listarTodos();
            for (VehiculoDTO v : existentes) {
                if (v.getMatricula().equalsIgnoreCase(dto.getMatricula())) {
                    throw new ErrorNegocio("Ya existe un vehículo con matrícula: " + dto.getMatricula());
                }
            }
            vehiculoService.registrarVehiculo(dto);
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al registrar el vehículo: " + e.getMessage());
        }
    }

    public void modificarVehiculo(VehiculoDTO dto) throws ErrorNegocio, MatriculaDuplicadaException {
        if (dto == null) {
            throw new ErrorNegocio("El DTO del vehículo no puede ser nulo.");
        }
        try {
            List<VehiculoDTO> existentes = vehiculoService.listarTodos();
            for (VehiculoDTO v : existentes) {
                if (v.getMatricula().equalsIgnoreCase(dto.getMatricula()) && v.getId() != dto.getId()) {
                    throw new MatriculaDuplicadaException("La matrícula " + dto.getMatricula() + " ya está registrada.");
                }
            }
            vehiculoService.actualizarVehiculo(dto);
        } catch (MatriculaDuplicadaException e) {
            // Se atrapa primero la excepción específica de negocio de matrícula
            throw e;
        } catch (ErrorNegocio e) {
            // Se atrapan las demás reglas generales de negocio
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al modificar el vehículo: " + e.getMessage());
        }
    }
    public void eliminarVehiculo(int id) {
        try {
            VehiculoDTO v = vehiculoService.buscarPorId(id);
            if (v != null) {
                vehiculoService.eliminarVehiculo(v.getMatricula());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<VehiculoDTO> listarVehiculosPorZona(int zonaId) {
        List<VehiculoDTO> todosLosVehiculos = vehiculoService.listarTodos();
        List<VehiculoDTO> resultado = new ArrayList<>();

        for (VehiculoDTO v : todosLosVehiculos) {
            var asignacion = asignacionVehiculoGarageService.buscarPorVehiculo(v.getId());

            if (asignacion != null && asignacion.getGarage().getZona().getId() == zonaId) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}