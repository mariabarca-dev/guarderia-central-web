
package exceptions;

/**
 * Excepción personalizada para manejar intentos de registro de socios 
 * con un DNI que ya existe en el sistema.
 */
public class DniDuplicadoException extends ErrorNegocio {

    // Constructor con un mensaje por defecto
    public DniDuplicadoException() {
        super("Error: El DNI ingresado ya se encuentra registrado en el sistema.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public DniDuplicadoException(String mensaje) {
        super(mensaje);
    }
}