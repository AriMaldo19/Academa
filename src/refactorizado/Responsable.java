package refactorizado;

public class Responsable {

    public static final Responsable SIN_ASIGNAR = new Responsable("-");

    private final String nombre;

    public Responsable(String nombre) {
        this.nombre = nombre;
    }

    public String nombre() {
        return nombre;
    }
}
