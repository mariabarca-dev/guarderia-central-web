package dao.impl;

import dao.*;
import database.ArchivoAsignacionVehiculoGarage;
import model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsignacionVehiculoGarageDAOImpl implements AsignacionVehiculoGarageDAO {
    
    private ArchivoAsignacionVehiculoGarage bd;

    public AsignacionVehiculoGarageDAOImpl() {
        this.bd = new ArchivoAsignacionVehiculoGarage();
        this.bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }

    private final String RUTA_ARCHIVO = "asignacionVehiculoGarage.txt";
    // Nota: Estos DAOs se usarán en listarTodas() para recuperar los objetos completos
    private VehiculoDAO vehiculoDAO = (VehiculoDAO) new VehiculoDAOImpl();
    private GarageDAO garageDAO = (GarageDAO) new GarageDAOImpl();

    @Override
    public void guardar(AsignacionVehiculoGarage asignacion) {
        System.out.println("Intentando guardar: " + asignacion.toCsv()); // DEBUG
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            // CAMBIO: Usamos el método toCsv() del modelo
            bw.write(asignacion.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AsignacionVehiculoGarage> listarTodas() {
        List<AsignacionVehiculoGarage> lista = new ArrayList<>();
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // Asumiendo que el CSV es: idVehiculo,idGarage,fechaAsignacion
                // (Ya que en el modelo corregimos toCsv() para usar IDs)
                Vehiculo v = vehiculoDAO.buscarPorId(Integer.parseInt(datos[0]));
                Garage g = garageDAO.buscarPorId(Integer.parseInt(datos[1]));
                
                // Validamos que existan antes de añadir
                if (v != null && g != null) {
                    lista.add(new AsignacionVehiculoGarage(v, g, LocalDate.parse(datos[2])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public AsignacionVehiculoGarage buscarPorGarage(Garage garage) {
        return listarTodas().stream()
                .filter(a -> a.getGarage().getId() == garage.getId()) // Filtramos por ID
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificar(AsignacionVehiculoGarage asignacion) {
        List<AsignacionVehiculoGarage> lista = listarTodas();
        for (int i = 0; i < lista.size(); i++) {
            // Comparamos por IDs para encontrar la asignación
            if (lista.get(i).getVehiculo().getId() == asignacion.getVehiculo().getId()
                    && lista.get(i).getGarage().getId() == asignacion.getGarage().getId()) {
                lista.set(i, asignacion);
                break;
            }
        }
        reescribirArchivo(lista);
    }

    @Override
    public void eliminar(String matricula, int numeroGarage) {
        // Nota: Esta implementación es un poco ineficiente al buscar por matrícula/número
        // pero funcionará si los DAOs de Vehiculo y Garage soportan esas búsquedas.
        // Para mayor robustez, sería ideal eliminar pasando los IDs exactos de la asignación.
        
        List<AsignacionVehiculoGarage> lista = listarTodas();
        lista.removeIf(a -> a.getVehiculo().getMatricula().equals(matricula)
                && a.getGarage().getNumeroGarage() == numeroGarage);
        reescribirArchivo(lista);
    }

    private void reescribirArchivo(List<AsignacionVehiculoGarage> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (AsignacionVehiculoGarage a : lista) {
                // CAMBIO: Usamos el método toCsv() del modelo
                bw.write(a.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}