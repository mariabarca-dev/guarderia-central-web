
package exceptions;

/**
 * Excepción personalizada para manejar situaciones donde una zona
 * ha alcanzado su capacidad máxima y no puede recibir más vehículos.
 */
public class ZonaSinCapacidadException extends ErrorNegocio {

    // Constructor vacío que carga un mensaje por defecto
    public ZonaSinCapacidadException() {
        super("Error: La zona seleccionada no tiene capacidad disponible para más vehículos.");
    }

    // Constructor que permite pasar un mensaje personalizado
    public ZonaSinCapacidadException(String mensaje) {
        super(mensaje);
    }
}