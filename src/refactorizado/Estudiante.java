package refactorizado;

public class Estudiante {

    private final String nombre;
    private final String legajo;

    public Estudiante(String nombre, String legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }

    public String nombre() {
        return nombre;
    }

    public String legajo() {
        return legajo;
    }
}
