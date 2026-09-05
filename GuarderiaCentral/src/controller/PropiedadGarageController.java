package controller;

import dto.GarageDTO;
import dto.PropiedadGarageDTO;
import exceptions.ErrorNegocio;
import service.PropiedadGarageService;
import model.Usuario;
import model.Socio;
import model.Rol;
import exceptions.GarageYaVendidoException;

import java.time.LocalDate;
import java.util.List;

public class PropiedadGarageController {

    private final PropiedadGarageService propiedadGarageService;

    public PropiedadGarageController(PropiedadGarageService propiedadGarageService) {
        this.propiedadGarageService = propiedadGarageService;
    }

    public void registrarPropiedad(Usuario usuarioSesion, PropiedadGarageDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new IllegalArgumentException("El objeto DTO no puede ser nulo.");
        }
        if (dto.getSocio() == null || dto.getSocio().getId() <= 0) {
            throw new IllegalArgumentException("Debe especificar un socio válido para la asignación de propiedad.");
        }
        if (dto.getGarage() == null || dto.getGarage().getNumeroGarage() <= 0) {
            throw new IllegalArgumentException("Debe especificar un garaje válido para la asignación de propiedad.");
        }
        if (dto.getFechaCompraGarage() == null) {
            throw new IllegalArgumentException("La fecha de compra no puede ser nula.");
        }
        if (dto.getFechaCompraGarage().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de compra no puede ser una fecha futura.");
        }

        propiedadGarageService.registrarPropiedad(dto);
    }

    public List<PropiedadGarageDTO> listarTodas(Usuario usuarioSesion) {
        validarUsuarioAutenticado(usuarioSesion);

        if (usuarioSesion.getRol() == Rol.SOCIO) {
            throw new SecurityException("Acceso denegado: Los socios no tienen permiso para ver el listado global de propiedades.");
        }

        return propiedadGarageService.listarTodas();
    }

    public String obtenerEstadoGarageSocio(Usuario usuarioSesion, int socioId) {
        validarUsuarioAutenticado(usuarioSesion);

        if (socioId <= 0) {
            throw new IllegalArgumentException("El ID de socio proporcionado no es válido.");
        }

        if (usuarioSesion.getRol() == Rol.SOCIO && usuarioSesion instanceof Socio socio) {
            if (socio.getId() != socioId) {
                throw new SecurityException("Acceso denegado: No tiene permisos para consultar el garaje de otro socio.");
            }
        }

        return propiedadGarageService.obtenerEstadoGarageSocio(socioId);
    }

    public List<GarageDTO> listarPorSocio(Usuario usuarioSesion, int socioId) {
        validarUsuarioAutenticado(usuarioSesion);

        if (socioId <= 0) {
            throw new IllegalArgumentException("El ID de socio proporcionado no es válido.");
        }

        if (usuarioSesion.getRol() == Rol.SOCIO && usuarioSesion instanceof Socio socio) {
            if (socio.getId() != socioId) {
                throw new SecurityException("Acceso denegado: Solo puede consultar sus propios garajes.");
            }
        }

        return propiedadGarageService.listarPorSocio(socioId);
    }

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarAdministrador(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de ADMINISTRADOR.");
        }
    }
}