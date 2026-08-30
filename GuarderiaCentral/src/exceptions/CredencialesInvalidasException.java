
package exceptions;

/**
 * Excepción personalizada para manejar intentos de inicio de sesión fallidos.

 */
public class CredencialesInvalidasException extends ErrorNegocio {

    // Constructor que recibe un mensaje
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}