
package exceptions;

/**
 * Excepción personalizada para manejar intentos de registro de empleados
 * con códigos que ya existen en el sistema.
 */
public class CodigoEmpleadoDuplicadoException extends ErrorNegocio {

    // Constructor vacío que carga un mensaje por defecto
    public CodigoEmpleadoDuplicadoException() {
        super("Error: El código de empleado ingresado ya existe en el sistema.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public CodigoEmpleadoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}