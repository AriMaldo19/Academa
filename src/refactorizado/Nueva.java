package refactorizado;

public class Nueva extends EstadoSolicitud {

    public String nombre() {
        return "Nueva";
    }

    public boolean puedePasarA(String destino) {
        return "En curso".equals(destino);
    }
}
