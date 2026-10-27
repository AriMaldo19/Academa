package refactorizado;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Coordina las operaciones y delega las reglas en las clases del dominio
// y la persistencia en ArchivoSolicitudes.
public class Gestor {

    private static final String NO_ENCONTRADA = "No se encontro la solicitud";

    private final ArchivoSolicitudes archivo;

    public Gestor(ArchivoSolicitudes archivo) {
        this.archivo = archivo;
    }

    public Resultado registrar(DatosSolicitud datos) {
        if (!datos.tieneDatosMinimos()) {
            return Resultado.fallo("Faltan datos de la solicitud");
        }
        TipoSolicitud tipo = TipoSolicitud.crear(datos.tipo());
        if (tipo == null) {
            return Resultado.fallo("Tipo de solicitud no valido");
        }
        if (!Solicitud.esPrioridadValida(datos.prioridad())) {
            return Resultado.fallo("Prioridad no valida");
        }
        List<Solicitud> solicitudes = archivo.leer();
        solicitudes.add(Solicitud.nueva(siguienteId(solicitudes), datos, tipo));
        archivo.guardar(solicitudes);
        return new Resultado(true, "Solicitud registrada", aDatos(solicitudes));
    }

    public Resultado cambiarEstado(String id, String nuevoEstado) {
        List<Solicitud> solicitudes = archivo.leer();
        Solicitud solicitud = buscarPorId(solicitudes, id);
        if (solicitud == null) {
            return new Resultado(false, NO_ENCONTRADA, aDatos(solicitudes));
        }
        if (!solicitud.puedeCambiarA(nuevoEstado)) {
            return new Resultado(false, "Esa transicion de estado no esta permitida", aDatos(solicitudes));
        }
        solicitud.cambiarEstado(nuevoEstado);
        archivo.guardar(solicitudes);
        return new Resultado(true, "Estado actualizado", aDatos(solicitudes));
    }

    public Resultado asignarResponsable(String id, String nombreResponsable) {
        if (nombreResponsable == null || nombreResponsable.trim().isEmpty()) {
            return Resultado.fallo("El responsable no puede estar vacio");
        }
        List<Solicitud> solicitudes = archivo.leer();
        Solicitud solicitud = buscarPorId(solicitudes, id);
        if (solicitud == null) {
            return new Resultado(false, NO_ENCONTRADA, aDatos(solicitudes));
        }
        solicitud.asignar(new Responsable(nombreResponsable.trim()));
        archivo.guardar(solicitudes);
        return new Resultado(true, "Responsable asignado", aDatos(solicitudes));
    }

    public Resultado buscar(String tipo, String prioridad, String estado) {
        List<Solicitud> encontradas = new ArrayList<>();
        for (Solicitud solicitud : archivo.leer()) {
            if (solicitud.coincide(tipo, prioridad, estado)) {
                encontradas.add(solicitud);
            }
        }
        return new Resultado(true, "", aDatos(encontradas));
    }

    public Resultado reporte() {
        List<Solicitud> solicitudes = archivo.leer();
        StringBuilder texto = new StringBuilder();
        for (Solicitud solicitud : solicitudes) {
            texto.append(solicitud.lineaReporte()).append("\n");
        }
        return new Resultado(true, texto.toString(), aDatos(solicitudes));
    }

    private String siguienteId(List<Solicitud> solicitudes) {
        int siguiente = 1;
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.id().matches("\\d+")) {
                siguiente = Math.max(siguiente, Integer.parseInt(solicitud.id()) + 1);
            }
        }
        return String.valueOf(siguiente);
    }

    private Solicitud buscarPorId(List<Solicitud> solicitudes, String id) {
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.tieneId(id)) {
                return solicitud;
            }
        }
        return null;
    }

    private List<Map<String, String>> aDatos(List<Solicitud> solicitudes) {
        List<Map<String, String>> datos = new ArrayList<>();
        for (Solicitud solicitud : solicitudes) {
            datos.add(solicitud.aMapa());
        }
        return datos;
    }
}
