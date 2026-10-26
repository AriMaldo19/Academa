package refactorizado;

// Strategy: cada tipo de solicitud sabe cuál es su etiqueta.
public interface TipoSolicitud {

    String etiqueta();

    // Devuelve null si el nombre no corresponde a un tipo válido.
    static TipoSolicitud crear(String nombre) {
        return switch (nombre) {
            case "Constancia" -> new Constancia();
            case "Equivalencia" -> new Equivalencia();
            case "Revision" -> new Revision();
            default -> null;
        };
    }
}
