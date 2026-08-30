
package exceptions;

/**
 * Excepción personalizada para manejar intentos de asignar un vehículo
 * a un garaje que ya tiene uno asignado.
 */
public class GarageYaOcupadoException extends ErrorNegocio {

    // Constructor vacío con un mensaje por defecto
    public GarageYaOcupadoException() {
        super("Error: El garaje seleccionado ya se encuentra ocupado por otro vehículo.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public GarageYaOcupadoException(String mensaje) {
        super(mensaje);
    }
}