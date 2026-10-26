package refactorizado;

// Agrupa los datos que llegan al registrar una solicitud (Introduce Parameter Object).
public class DatosSolicitud {

    private final Estudiante estudiante;
    private final String tipo;
    private final String prioridad;
    private final String descripcion;

    public DatosSolicitud(String nombre, String legajo, String tipo, String prioridad, String descripcion) {
        this.estudiante = new Estudiante(limpiar(nombre), limpiar(legajo));
        this.tipo = limpiar(tipo);
        this.prioridad = limpiar(prioridad);
        this.descripcion = limpiar(descripcion);
    }

    public boolean tieneDatosMinimos() {
        return !estudiante.nombre().isEmpty()
                && !estudiante.legajo().isEmpty()
                && !tipo.isEmpty()
                && !prioridad.isEmpty()
                && !descripcion.isEmpty();
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

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
