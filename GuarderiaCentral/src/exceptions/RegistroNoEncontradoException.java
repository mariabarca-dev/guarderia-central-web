package exceptions;

/**
 * Excepción personalizada para manejar situaciones donde se intenta buscar 
 * un registro (socio, empleado, vehículo, etc.) y este no existe en el sistema.
 */
public class RegistroNoEncontradoException extends ErrorNegocio {

    // Constructor vacío con un mensaje por defecto
    public RegistroNoEncontradoException() {
        super("Error: No se encontró el registro solicitado en el sistema.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public RegistroNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}