package dao.impl;

import dao.AdministradorDAO;
import database.ArchivoAdministrador;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;

public class AdministradorDAOImpl implements AdministradorDAO {

    private final String RUTA_ARCHIVO = "administrador.txt";

    public AdministradorDAOImpl() {
        ArchivoAdministrador bd = new ArchivoAdministrador();
        bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }

    @Override
    public void guardar(Administrador admin) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(admin.toCsv());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar administrador: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Administrador admin) {
        List<Administrador> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == admin.getId()) {
                lista.set(i, admin);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null) return;
        List<Administrador> lista = listarTodos();
        lista.removeIf(a -> a.getId() == id);
        reescribirArchivo(lista);
    }

    @Override
    public Administrador buscarPorId(Integer id) {
        if (id == null) return null;
        return listarTodos().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Administrador> listarTodos() {
        List<Administrador> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Ignora líneas vacías o con puros espacios
                if (linea.trim().isEmpty()) {
                    continue;
                }

                try {
                    Administrador admin = Administrador.fromString(linea);
                    lista.add(admin);
                } catch (Exception e) {
                    System.err.println("Error al procesar línea en administrador.txt: \"" + linea + "\" - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de administradores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean existeAdministrador(String username) {
        if (username == null) return false;
        return listarTodos().stream()
                .anyMatch(a -> a.getNombreUsuario() != null && a.getNombreUsuario().equalsIgnoreCase(username));
    }

    private void reescribirArchivo(List<Administrador> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Administrador a : lista) {
                bw.write(a.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir el archivo de administradores: " + e.getMessage());
        }
    }
}