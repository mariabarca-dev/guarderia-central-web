package dao.impl;

import dao.GarageDAO;
import dao.SocioDAO;
import dao.ZonaDAO;
import database.ArchivoGarage;
import model.Garage;
import model.Socio;
import model.Zona;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GarageDAOImpl implements GarageDAO {

    private final String RUTA_ARCHIVO = ArchivoGarage.getARCHIVO();

    private final ArchivoGarage bd;
    private final SocioDAO socioDAO = new SocioDAOImpl();
    private final ZonaDAO zonaDAO = new ZonaDAOImpl();

    public GarageDAOImpl() {
        this.bd = new ArchivoGarage();
        this.bd.inicializarBD();
    }

    @Override
    public void guardar(Garage garage) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(garage.toCsv());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar en " + RUTA_ARCHIVO + ": " + e.getMessage());
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
                if (linea.trim().isEmpty()) continue;

                // Una línea corrupta no debe tirar abajo el listado completo
                try {
                    Garage garage = construirDesdeLinea(linea);
                    if (garage != null) {
                        lista.add(garage);
                    }
                } catch (RuntimeException ex) {
                    System.err.println("Línea inválida en " + RUTA_ARCHIVO + " (se omite): " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + RUTA_ARCHIVO + ": " + e.getMessage());
        }
        return lista;
    }

    /**
     * Resuelve las relaciones (Socio y Zona) de una línea y arma el Garage.
     * Formato CSV: id,numeroGarage,lecturaLuz,servicioMantenimiento,idSocio,fechaCompra,idZona
     */
    private Garage construirDesdeLinea(String linea) {
        String[] datos = linea.split(",");

        int idSocio = Integer.parseInt(datos[4].trim());
        Socio socio = (idSocio != 0) ? socioDAO.buscarPorId(idSocio) : null;

        int idZona = Integer.parseInt(datos[6].trim());
        Zona zona = zonaDAO.buscarPorId(idZona);

        // Sin zona el garaje no se puede construir: el modelo la exige
        if (zona == null) {
            System.err.println("Se omite el garaje de la línea (zona " + idZona + " inexistente): " + linea);
            return null;
        }

        return Garage.fromString(linea, socio, zona);
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

    @Override
    public List<Garage> listarPorSocio(int socioId) {
        return listarTodos().stream()
                .filter(g -> g.getSocioPropietario() != null
                        && g.getSocioPropietario().getId() == socioId)
                .collect(Collectors.toList());
    }

    private void reescribirArchivo(List<Garage> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Garage g : lista) {
                bw.write(g.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir " + RUTA_ARCHIVO + ": " + e.getMessage());
        }
    }
}