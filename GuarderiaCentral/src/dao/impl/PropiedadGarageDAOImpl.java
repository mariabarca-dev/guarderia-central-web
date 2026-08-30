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
    private final String RUTA_ARCHIVO = "propiedades_garage.txt";
    private SocioDAO socioDAO = (SocioDAO) new SocioDAOImpl();
    // CORRECCIÓN: Usamos GarageDAO genérico para poder buscar por ID
    private GarageDAO garageDAO = (GarageDAO) new GarageDAOImpl(); 

    @Override
    public void guardar(PropiedadGarage propiedad) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            // CAMBIO: Delegamos el formato al modelo
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
                String[] datos = linea.split(",");
                // Asumiendo formato CSV: idSocio,idGarage,fechaCompra
                Socio s = socioDAO.buscarPorId(Integer.parseInt(datos[0]));
                // CORRECCIÓN: Buscamos por ID, no por número de garage, para coincidir con toCsv()
                Garage g = garageDAO.buscarPorId(Integer.parseInt(datos[1]));
                
                if (s != null && g != null) {
                    lista.add(new PropiedadGarage(s, g, LocalDate.parse(datos[2])));
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
        // Filtramos para eliminar la relación que coincida con ambos criterios
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
            // Identificamos la propiedad por el ID del Socio y el número del Garaje
            if (p.getSocio().getId() == propiedad.getSocio().getId() && 
                p.getGarage().getNumeroGarage() == propiedad.getGarage().getNumeroGarage()) {
                
                lista.set(i, propiedad); // Reemplazamos con el objeto actualizado
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
                // CAMBIO: Delegamos el formato al modelo
                bw.write(p.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}