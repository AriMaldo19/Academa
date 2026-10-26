package refactorizado;

import java.util.LinkedHashMap;
import java.util.Map;

public class Solicitud {

    private final String id;
    private final Estudiante estudiante;
    private final String tipo;
    private final String prioridad;
    private final String descripcion;
    private EstadoSolicitud estado;
    private Responsable responsable;
    private final String etiqueta;
    private final String marca;

    public Solicitud(String id, Estudiante estudiante, String tipo, String prioridad, String descripcion,
                     EstadoSolicitud estado, Responsable responsable, String etiqueta, String marca) {
        this.id = id;
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.estado = estado;
        this.responsable = responsable;
        this.etiqueta = etiqueta;
        this.marca = marca;
    }

    public static Solicitud nueva(String id, DatosSolicitud datos, TipoSolicitud tipo) {
        return new Solicitud(id, datos.estudiante(), datos.tipo(), datos.prioridad(), datos.descripcion(),
                new Nueva(), Responsable.SIN_ASIGNAR, tipo.etiqueta(), marcaDe(datos.prioridad()));
    }

    public static String marcaDe(String prioridad) {
        return switch (prioridad) {
            case "Alta" -> "URGENTE";
            case "Media" -> "NORMAL";
            case "Baja" -> "BAJA";
            default -> "";
        };
    }

    public static boolean esPrioridadValida(String prioridad) {
        return !marcaDe(prioridad).isEmpty();
    }

    public boolean tieneId(String identificador) {
        return id.equals(identificador);
    }

    public boolean puedeCambiarA(String nuevoEstado) {
        return estado.puedePasarA(nuevoEstado);
    }

    public void cambiarEstado(String nuevoEstado) {
        estado = EstadoSolicitud.crear(nuevoEstado);
    }

    public void asignar(Responsable nuevoResponsable) {
        responsable = nuevoResponsable;
    }

    // Un filtro vacío no filtra.
    public boolean coincide(String tipoBuscado, String prioridadBuscada, String estadoBuscado) {
        return cumpleFiltro(tipoBuscado, tipo)
                && cumpleFiltro(prioridadBuscada, prioridad)
                && cumpleFiltro(estadoBuscado, estado.nombre());
    }

    public String lineaReporte() {
        return String.join(" | ", id, estudiante.nombre(), tipo, prioridad, estado.nombre(),
                responsable.nombre(), etiqueta, marca);
    }

    public Map<String, String> aMapa() {
        Map<String, String> fila = new LinkedHashMap<>();
        fila.put("id", id);
        fila.put("nombre", estudiante.nombre());
        fila.put("legajo", estudiante.legajo());
        fila.put("tipo", tipo);
        fila.put("prioridad", prioridad);
        fila.put("descripcion", descripcion);
        fila.put("estado", estado.nombre());
        fila.put("responsable", responsable.nombre());
        return fila;
    }

    public String id() {
        return id;
    }

    public Estudiante estudiante() {
        return estudiante;
    }

    public String tipo() {
        return tipo;
    }

    public String prioridad() {
        return prioridad;
    }

    public String descripcion() {
        return descripcion;
    }

    public String nombreEstado() {
        return estado.nombre();
    }

    public Responsable responsable() {
        return responsable;
    }

    public String etiqueta() {
        return etiqueta;
    }

    public String marca() {
        return marca;
    }

    private static boolean cumpleFiltro(String filtro, String valor) {
        return filtro == null || filtro.isEmpty() || filtro.equals(valor);
    }
}
