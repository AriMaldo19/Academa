package refactorizado;

public class EnCurso extends EstadoSolicitud {

    public String nombre() {
        return "En curso";
    }

    public boolean puedePasarA(String destino) {
        return "Resuelta".equals(destino) || "Rechazada".equals(destino);
    }
}
