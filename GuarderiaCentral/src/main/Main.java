package main;

import view.impl.MenuLoginImpl;
import util.InicializarDataBase; // Importar la clase de inicialización
import service.AdministradorService; // Importar servicios necesarios
import service.EmpleadoService;
import service.SocioService;
import exceptions.ErrorNegocio; // Importar excepción de negocio

public class Main {

    public static void main(String[] args) {

        // 1. Inicializar la Base de Datos con usuarios por defecto
        try {
            // Crear instancias de los servicios necesarios
            AdministradorService adminService = new AdministradorService();
            EmpleadoService empleadoService = new EmpleadoService();
            SocioService socioService = new SocioService();

            // Inicializar la base de datos pasando los servicios inyectados
            InicializarDataBase initDb = new InicializarDataBase(adminService, empleadoService, socioService);
            initDb.verificarConfiguracionInicial(); // Ejecutar la verificación
            System.out.println("Base de datos inicializada correctamente con usuarios por defecto.");

        } catch (ErrorNegocio e) {
            System.err.println("Error de negocio al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
            return; // Salir si la inicialización falla por lógica de negocio
        } catch (Exception e) {
            System.err.println("Error inesperado al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
            return; // Salir si la inicialización falla por error técnico
        }
        while(true){
            MenuLoginImpl loginView = new MenuLoginImpl();
            // El login llama a los otros menús internamente
            loginView.mostrar();
            // Finalización de la aplicación
            System.out.println("Aplicación finalizada.");
        }
        
    }
}
