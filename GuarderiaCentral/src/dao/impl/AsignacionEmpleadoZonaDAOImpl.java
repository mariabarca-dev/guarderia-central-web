package dao.impl;

import dao.AsignacionEmpleadoZonaDAO;
import dao.EmpleadoDAO;
import dao.ZonaDAO;
import database.ArchivoAsignacionEmpleadoZona;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.AsignacionEmpleadoZona;
import model.Empleado;
import model.Zona;

public class AsignacionEmpleadoZonaDAOImpl implements AsignacionEmpleadoZonaDAO {
    
    private ArchivoAsignacionEmpleadoZona bd;

    public AsignacionEmpleadoZonaDAOImpl() {
        this.bd = new ArchivoAsignacionEmpleadoZona();
        this.bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }

    private final String RUTA_ARCHIVO = "asignacionEmpleadoZona.txt";
    private EmpleadoDAO empleadoDAO = (EmpleadoDAO) new EmpleadoDAOImpl();
    private ZonaDAO zonaDAO = (ZonaDAO) new ZonaDAOImpl();

@Override
    public void guardar(AsignacionEmpleadoZona asignacion) {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            // CAMBIO: Ahora el DAO no sabe cómo se forma el string, solo le pide al objeto que lo haga
            bw.write(asignacion.toCsv());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificar(AsignacionEmpleadoZona asignacion) {
        List<AsignacionEmpleadoZona> lista = listarTodas();
        boolean encontrado = false;
        
        for (int i = 0; i < lista.size(); i++) {
            AsignacionEmpleadoZona a = lista.get(i);
            if(a.getEmpleado().getCodigo().equals(asignacion.getEmpleado().getCodigo()) && a.getZona().getId() == asignacion.getZona().getId()){
                lista.set(i, asignacion);
                encontrado = true;
                break;
            }
        }
        if(encontrado){
            reescribirArchivo(lista);
        }
    }

    @Override
    public void eliminar(String codigoEmpleado, int zonaId) {
        List<AsignacionEmpleadoZona> lista = listarTodas();
        
        boolean removido = lista.removeIf(a -> a.getEmpleado().getCodigo().equals(codigoEmpleado) && a.getZona().getId() == zonaId);
        if(removido){
            reescribirArchivo(lista);
        }
    }
    
   private void reescribirArchivo(List<AsignacionEmpleadoZona> lista) {
       
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (AsignacionEmpleadoZona a : lista) {
                // CAMBIO: Usamos el método unificado toCsv()
                bw.write(a.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AsignacionEmpleadoZona> listarTodas() {
        List<AsignacionEmpleadoZona> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                Empleado e = empleadoDAO.buscarPorCodigo(datos[0]);
                Zona z = zonaDAO.buscarPorId(Integer.parseInt(datos[1]));
                lista.add(new AsignacionEmpleadoZona(e, z, Integer.parseInt(datos[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<AsignacionEmpleadoZona> buscarPorEmpleado(String codigoEmpleado) {
        List<AsignacionEmpleadoZona> filtradas = new ArrayList<>();
        for (AsignacionEmpleadoZona a : listarTodas()) {
            if (a.getEmpleado().getCodigo().equals(codigoEmpleado)) {
                filtradas.add(a);
            }
        }
        return filtradas;
    }

    @Override
    public int contarVehiculosEnZona(int zonaId) {
        int total = 0;
        for (AsignacionEmpleadoZona a : listarTodas()) {
            if (a.getZona().getId() == zonaId) {
                total += a.getCantVehiculosACargo();
            }
        }
        return total;
    }

}

