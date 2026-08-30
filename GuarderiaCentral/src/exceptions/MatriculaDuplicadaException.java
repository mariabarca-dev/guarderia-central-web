
package exceptions;

/**
 * Excepción personalizada para manejar intentos de registro de vehículos
 * con una matrícula que ya existe en el sistema.
 */
public class MatriculaDuplicadaException extends ErrorNegocio {

    // Constructor vacío con un mensaje por defecto
    public MatriculaDuplicadaException() {
        super("Error: La matrícula ingresada ya se encuentra registrada en el sistema.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public MatriculaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
