package dao.impl;

import dao.EmpleadoDAO;
import database.ArchivoEmpleado;
import model.Empleado;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOImpl implements EmpleadoDAO {
    
    private ArchivoEmpleado bd;

    public EmpleadoDAOImpl() {
        this.bd = new ArchivoEmpleado();
        this.bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }
    
    private final String RUTA_ARCHIVO = "empleado.txt";

    @Override
    public void guardar(Empleado empleado) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(empleado.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Empleado empleado) {
        List<Empleado> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == empleado.getId()) {
                lista.set(i, empleado);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(Integer id) {
        List<Empleado> lista = listarTodos();
        lista.removeIf(e -> e.getId() == id);
        reescribirArchivo(lista);
    }

    @Override
    public Empleado buscarPorId(Integer id) {
        return listarTodos().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Empleado.fromString(linea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Empleado buscarPorCodigo(String codigo) {
        return listarTodos().stream()
                .filter(e -> e.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }


    private void reescribirArchivo(List<Empleado> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Empleado e : lista) {
                bw.write(e.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
