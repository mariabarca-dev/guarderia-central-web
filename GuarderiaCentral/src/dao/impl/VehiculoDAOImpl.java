package dao.impl;

import dao.VehiculoDAO;
import database.ArchivoVehiculo;
import model.Vehiculo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class VehiculoDAOImpl implements VehiculoDAO{
    
    private ArchivoVehiculo bd;

    public VehiculoDAOImpl() {
        this.bd = new ArchivoVehiculo();
        // Nota: ArchivoVehiculo.inicializarBD() suele crear el archivo si no existe.
        this.bd.inicializarBD(); 
    }
    
    // CORRECCIÓN: Ajustamos la ruta para que coincida con la estándar del sistema 
    // (normalmente singular sin 's', ej: empleado.txt, vehiculo.txt)
    private final String RUTA_ARCHIVO = "vehiculo.txt";

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
            // Asumimos que la matrícula es el identificador único de negocio
            if (lista.get(i).getMatricula().equals(vehiculo.getMatricula())) {
                lista.set(i, vehiculo);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(String matricula) {
        List<Vehiculo> lista = listarTodos();
        lista.removeIf(v -> v.getMatricula().equals(matricula));
        reescribirArchivo(lista);
    }

    @Override
    public Vehiculo buscarPorId(int id) {
        return listarTodos().stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Vehiculo buscarPorMatricula(String matricula) {
        return listarTodos().stream()
                .filter(v -> v.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    /**
     * --- CAMBIO 1: MÉTODO NUEVO PARA EL MENÚ EMPLEADO ---
     * Busca todos los vehículos asignados a un empleado específico.
     */
    @Override
    public List<Vehiculo> buscarPorEmpleado(int empleadoId) {
        // Filtramos la lista completa usando el nuevo campo empleadoId
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
                if (linea.trim().isEmpty()) continue; // Evitar líneas vacías
                lista.add(Vehiculo.fromString(linea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void reescribirArchivo(List<Vehiculo> lista) {
        // Sobrescribe el archivo completamente con la lista actualizada
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