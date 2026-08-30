
package exceptions;

/**
 * Excepción personalizada para manejar intentos de comprar o asignar 
 * un garaje que ya tiene un propietario registrado.
 */
public class GarageYaVendidoException extends ErrorNegocio {

    // Constructor vacío con un mensaje por defecto
    public GarageYaVendidoException() {
        super("Error: El garaje seleccionado ya ha sido vendido a otro socio.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public GarageYaVendidoException(String mensaje) {
        super(mensaje);
    }
}