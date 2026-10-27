package refactorizado;

import interfaz.Servidor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Main {

    private static final String ARCHIVO = "solicitudes-refactorizada.txt";
    private static final Gestor GESTOR = new Gestor(new ArchivoSolicitudes(ARCHIVO));

    public static void main(String[] args) throws IOException {
        Servidor.iniciar(8082, "refactorizada", "Academa — versión refactorizada", new Servidor.Api() {
            public String registrar(String nombre, String legajo, String tipo, String prioridad, String descripcion) {
                return responder(() -> GESTOR.registrar(new DatosSolicitud(nombre, legajo, tipo, prioridad, descripcion)));
            }

            public String cambiarEstado(String id, String estado) {
                return responder(() -> GESTOR.cambiarEstado(id, estado));
            }

            public String asignar(String id, String responsable) {
                return responder(() -> GESTOR.asignarResponsable(id, responsable));
            }

            public String buscar(String tipo, String prioridad, String estado) {
                return responder(() -> GESTOR.buscar(tipo, prioridad, estado));
            }

            public String reporte() {
                return responder(GESTOR::reporte);
            }
        });
    }

    // Los errores del archivo se informan con su causa en lugar de ocultarse.
    private static String responder(Supplier<Resultado> operacion) {
        try {
            Resultado resultado = operacion.get();
            return Servidor.json(resultado.ok, resultado.mensaje, resultado.datos);
        } catch (IllegalStateException ex) {
            return Servidor.json(false, ex.getMessage(), new ArrayList<>());
        }
    }
}
