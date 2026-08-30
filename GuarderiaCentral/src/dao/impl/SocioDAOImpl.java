package dao.impl;

import dao.SocioDAO;
import database.ArchivoSocio;
import model.Socio;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAOImpl implements SocioDAO {
    
    private ArchivoSocio bd;

    public SocioDAOImpl() {
        this.bd = new ArchivoSocio();
        this.bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }
    
    private final String RUTA_ARCHIVO = "socios.txt";

    @Override
    public void guardar(Socio socio) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(socio.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Socio socio) {
        List<Socio> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == socio.getId()) {
                lista.set(i, socio);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(Integer id) {
        List<Socio> lista = listarTodos();
        lista.removeIf(s -> s.getId() == id);
        reescribirArchivo(lista);
    }

    @Override
    public Socio buscarPorId(Integer id) {
        return listarTodos().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Socio> listarTodos() {
        List<Socio> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Socio.fromString(linea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Socio buscarPorDni(String dni) {
        return listarTodos().stream()
                .filter(s -> s.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }


    private void reescribirArchivo(List<Socio> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Socio s : lista) {
                bw.write(s.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
