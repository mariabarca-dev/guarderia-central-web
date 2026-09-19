package dao.impl;

import dao.VehiculoDAO;
import database.ArchivoVehiculo;
import model.Vehiculo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VehiculoDAOImpl implements VehiculoDAO {

    private ArchivoVehiculo bd;
    private final String RUTA_ARCHIVO = "vehiculo.txt";

    public VehiculoDAOImpl() {
        this.bd = new ArchivoVehiculo();
        this.bd.inicializarBD();
    }

    @Override
    public void guardar(Vehiculo vehiculo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(vehiculo.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Vehiculo vehiculo) {
        List<Vehiculo> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getMatricula().equals(vehiculo.getMatricula())) {
                lista.set(i, vehiculo);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null) return;
        List<Vehiculo> lista = listarTodos();
        lista.removeIf(v -> id.equals(v.getId()));
        reescribirArchivo(lista); // Guarda los cambios en el archivo .txt
    }

    @Override
    public void eliminar(String matricula) {
        if (matricula == null) return;
        List<Vehiculo> lista = listarTodos();
        lista.removeIf(v -> v.getMatricula().equals(matricula));
        reescribirArchivo(lista);
    }

    @Override
    public Vehiculo buscarPorId(Integer id) {
        if (id == null) return null;
        return listarTodos().stream()
                .filter(v -> id.equals(v.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Vehiculo buscarPorMatricula(String matricula) {
        if (matricula == null) return null;
        return listarTodos().stream()
                .filter(v -> v.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Vehiculo> buscarPorEmpleado(int empleadoId) {
        return listarTodos().stream()
                .filter(v -> v.getEmpleadoId() == empleadoId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehiculo> listarTodos() {
        List<Vehiculo> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                lista.add(Vehiculo.fromString(linea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void reescribirArchivo(List<Vehiculo> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Vehiculo v : lista) {
                bw.write(v.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}