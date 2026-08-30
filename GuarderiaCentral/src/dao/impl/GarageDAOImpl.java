package dao.impl;

import dao.GarageDAO;
import dao.SocioDAO;
import dao.ZonaDAO; // Import necesario
import dao.impl.SocioDAOImpl;
import dao.impl.ZonaDAOImpl; // Import necesario
import database.ArchivoGarage;
import model.Garage;
import model.Socio;
import model.Zona; // Import necesario
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GarageDAOImpl implements GarageDAO {

    private ArchivoGarage bd;
    private SocioDAO socioDAO = new SocioDAOImpl();
    private ZonaDAO zonaDAO = new ZonaDAOImpl(); // Inyección de ZonaDAO

    public GarageDAOImpl() {
        this.bd = new ArchivoGarage();
        this.bd.inicializarBD();
    }

    private final String RUTA_ARCHIVO = "garage.txt";

    @Override
    public void guardar(Garage garage) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(garage.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Garage buscarPorId(int id) {
        return listarTodos().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Garage buscarPorNumero(int numeroGarage) {
        return listarTodos().stream()
                .filter(g -> g.getNumeroGarage() == numeroGarage)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Garage> listarTodos() {
        List<Garage> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                // Formato CSV: id,numeroGarage,lecturaLuz,servicioMantenimiento,idSocio,fechaCompra,idZona
                // datos[4] = idSocio, datos[6] = idZona
                int idSocio = Integer.parseInt(datos[4]);
                Socio socio = (idSocio != 0) ? socioDAO.buscarPorId(idSocio) : null;

                int idZona = Integer.parseInt(datos[6]);
                Zona zona = zonaDAO.buscarPorId(idZona);

                // Pasamos la línea completa y los objetos ya recuperados
                if (zona != null) {
                    lista.add(Garage.fromString(linea, socio, zona));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void actualizar(Garage garage) {
        List<Garage> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNumeroGarage() == garage.getNumeroGarage()) {
                lista.set(i, garage);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(int numeroGarage) {
        List<Garage> lista = listarTodos();
        lista.removeIf(g -> g.getNumeroGarage() == numeroGarage);
        reescribirArchivo(lista);
    }

    private void reescribirArchivo(List<Garage> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Garage g : lista) {
                bw.write(g.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Garage> listarPorSocio(int socioId) {
        // 1. Llamamos a listarTodos() para obtener la lista desde el archivo
        // 2. Filtramos la lista por el ID del socio
        return listarTodos().stream()
                .filter(g -> g.getSocioPropietario() != null && g.getSocioPropietario().getId() == socioId) 
                // Nota: Ajusta 'g.getSocioPropietario().getId()' 
                // según cómo se llame el método en tu modelo Garage para obtener el ID del socio.
                .collect(Collectors.toList());
    }

}
