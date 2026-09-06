package dao.impl;

import dao.ZonaDAO;
import database.ArchivoZona;
import model.Zona;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ZonaDAOImpl implements ZonaDAO {

    private ArchivoZona bd;

    public ZonaDAOImpl() {
        this.bd = new ArchivoZona();
        this.bd.inicializarBD();
    }

    private final String RUTA_ARCHIVO = "zona.txt";

    @Override
    public void guardar(Zona zona) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(zona.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Zona zona) {
        List<Zona> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getLetra().equals(zona.getLetra())) {
                lista.set(i, zona);
                break;
            }
        }
        reescribirArchivo(lista);
    }
//dani
    // Cumple con GenericDAO<Zona, Integer>
    @Override
    public void eliminar(Integer id) {
        if (id == null) return;
        List<Zona> lista = listarTodos();
        lista.removeIf(z -> z.getId() == id);
        reescribirArchivo(lista);
    }

    // Mantiene tu método original por si lo usas con la letra de la zona
    @Override
    public void eliminar(String letra) {
        List<Zona> lista = listarTodos();
        lista.removeIf(z -> z.getLetra().equals(letra));
        reescribirArchivo(lista);
    }

    @Override
    public Zona buscarPorLetra(String letra) {
        if (letra == null) return null;
        return listarTodos().stream()
                .filter(z -> z.getLetra() != null && z.getLetra().equalsIgnoreCase(letra.trim()))
                .findFirst()
                .orElse(null);
    }

    // Cumple con GenericDAO<Zona, Integer> (cambiado 'int' por 'Integer')
    @Override
    public Zona buscarPorId(Integer id) {
        if (id == null) return null;
        return listarTodos().stream()
                .filter(z -> z.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Zona> listarTodos() {
        List<Zona> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Zona.fromString(linea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Zona buscarPorId(int id) {
        return null;
    }

    private void reescribirArchivo(List<Zona> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Zona z : lista) {
                bw.write(z.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}