
package view.impl;


import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class VistaImpl {
    // Todos los menús compartirán este mismo scanner
    protected Scanner scanner = new Scanner(System.in);
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Método para imprimir encabezados iguales en todas las vistas
    protected void imprimirEncabezado(String titulo) {
        System.out.println("================================");
        System.out.println("   " + titulo);
        System.out.println("================================");
    }

    // Método para esperar respuesta del usuario
    protected void presionarParaContinuar() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    protected String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim();
    }

    protected int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número entero válido.");
            }
        }
    }

    protected double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número decimal válido.");
            }
        }
    }

    protected LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " (DD/MM/AAAA): ");
                return LocalDate.parse(scanner.nextLine().trim(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Use DD/MM/AAAA.");
            }
        }
    }
    
    // Obligas a que todos los menús tengan un método para iniciar
    public abstract void mostrar();
}