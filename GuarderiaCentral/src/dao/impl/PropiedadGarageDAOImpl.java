package dao.impl;

import dao.*;
import database.ArchivoPropiedadGarage;
import model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PropiedadGarageDAOImpl implements PropiedadGarageDAO {

    private ArchivoPropiedadGarage bd;

    public PropiedadGarageDAOImpl() {
        this.bd = new ArchivoPropiedadGarage();
        this.bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }

    // Sincronizado exactamente con el nombre unificado de la base de datos
    private final String RUTA_ARCHIVO = ArchivoPropiedadGarage.getARCHIVO();
    private SocioDAO socioDAO = new SocioDAOImpl();
    private GarageDAO garageDAO = new GarageDAOImpl();

    @Override
    public void guardar(PropiedadGarage propiedad) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(propiedad.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PropiedadGarage> listarTodas() {
        List<PropiedadGarage> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(",");

                Socio s = socioDAO.buscarPorId(Integer.parseInt(datos[0].trim()));
                Garage g = garageDAO.buscarPorId(Integer.parseInt(datos[1].trim()));

                if (s != null && g != null) {
                    lista.add(new PropiedadGarage(s, g, LocalDate.parse(datos[2].trim())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public PropiedadGarage buscarPorSocio(int socioId) {
        return listarTodas().stream()
                .filter(p -> p.getSocio().getId() == socioId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void eliminar(int socioId, int numeroGarage) {
        List<PropiedadGarage> lista = listarTodas();
        boolean removido = lista.removeIf(p ->
                p.getSocio().getId() == socioId &&
                        p.getGarage().getNumeroGarage() == numeroGarage
        );

        if (removido) {
            reescribirArchivo(lista);
        }
    }

    @Override
    public void actualizar(PropiedadGarage propiedad) {
        List<PropiedadGarage> lista = listarTodas();
        boolean encontrado = false;

        for (int i = 0; i < lista.size(); i++) {
            PropiedadGarage p = lista.get(i);
            if (p.getSocio().getId() == propiedad.getSocio().getId() &&
                    p.getGarage().getNumeroGarage() == propiedad.getGarage().getNumeroGarage()) {

                lista.set(i, propiedad);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            reescribirArchivo(lista);
        }
    }

    private void reescribirArchivo(List<PropiedadGarage> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (PropiedadGarage p : lista) {
                bw.write(p.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


