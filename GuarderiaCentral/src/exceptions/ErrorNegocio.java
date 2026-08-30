
package exceptions;

/**
 * Excepción base para errores de lógica de negocio en el sistema de la Guardería Central.
 * Todas las excepciones que representen una violación a las reglas de negocio 
 * (como duplicidad de datos, fechas inválidas, etc.) deberían extender de esta clase.
 */
public class ErrorNegocio extends Exception {

    // Constructor que recibe solo el mensaje del error
    public ErrorNegocio(String mensaje) {
        super(mensaje);
    }

    // Constructor que recibe el mensaje y la causa original (útil para depuración)
    public ErrorNegocio(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
