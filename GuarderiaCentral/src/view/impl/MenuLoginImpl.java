package view.impl;

import controller.LoginController;
// Se eliminan las importaciones de UsuarioService y CredencialesInvalidasException
// ya que la vista ya no las maneja directamente

public class MenuLoginImpl extends VistaImpl {

    // Se elimina la instancia de UsuarioService
    @Override
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            imprimirEncabezado("GUARDERÍA CENTRAL - INICIO DE SESIÓN");

            String usuarioIngresado = leerTexto("Ingrese Nombre de Usuario (o 'salir' para cerrar)");

            if (usuarioIngresado.equalsIgnoreCase("salir")) {
                System.out.println("Cerrando aplicación...");
                System.exit(0); // Este comando mata el proceso de Java instantáneamente
            }
            
            String claveIngresada = leerTexto("Ingrese Clave");

            // Instancia del controlador
            LoginController controlador = new LoginController();
            try {
                // 🔹 Delegar toda la autenticación y lógica al controlador
                // El controlador validará, obtendrá el usuario específico y abrirá el menú.
                controlador.login(usuarioIngresado, claveIngresada);

                // Si el login es exitoso, el controlador mostrará el menú y el bucle se corta.
                salir = true; // ✅ se corta el bucle de login

                // Se captura la excepción genérica Exception, ya que el controlador
                // gestiona SecurityException y otras excepciones
            } catch (Exception e) {
                // El controlador ya habrá impreso el mensaje de error correspondiente.
                presionarParaContinuar();
                // No se sale del bucle, permitiendo reintentar el login.
            }
        }
    }
}
