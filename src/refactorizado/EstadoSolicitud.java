package refactorizado;

// State: cada estado define a qué estados puede pasar una solicitud.
public abstract class EstadoSolicitud {

    public abstract String nombre();

    // Por defecto no se permite ninguna transición (estados terminales).
    public boolean puedePasarA(String destino) {
        return false;
    }

    public static EstadoSolicitud crear(String nombre) {
        return switch (nombre) {
            case "Nueva" -> new Nueva();
            case "En curso" -> new EnCurso();
            case "Resuelta" -> new Resuelta();
            case "Rechazada" -> new Rechazada();
            default -> new EstadoSolicitud() {
                public String nombre() {
                    return nombre;
                }
            };
        };
    }
}
