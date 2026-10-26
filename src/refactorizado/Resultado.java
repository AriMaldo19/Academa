package refactorizado;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Resultado {

    public final boolean ok;
    public final String mensaje;
    public final List<Map<String, String>> datos;

    public Resultado(boolean ok, String mensaje, List<Map<String, String>> datos) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public static Resultado fallo(String mensaje) {
        return new Resultado(false, mensaje, new ArrayList<>());
    }
}
