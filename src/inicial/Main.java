package inicial;

import interfaz.Servidor;
import java.io.IOException;

public class Main {

    private static final String ARCHIVO = "solicitudes-inicial.txt";

    public static void main(String[] args) throws IOException {
        Servidor.iniciar(8081, "inicial", "Academa — versión inicial", new Servidor.Api() {
            public String registrar(String nombre, String legajo, String tipo, String prioridad, String descripcion) {
                Resultado resultado = Gestor.registrar(nombre, legajo, tipo, prioridad, descripcion, ARCHIVO);
                return Servidor.json(resultado.ok, resultado.mensaje, resultado.datos);
            }

            public String cambiarEstado(String id, String estado) {
                Resultado resultado = Gestor.cambiarEstado(id, estado, ARCHIVO);
                return Servidor.json(resultado.ok, resultado.mensaje, resultado.datos);
            }

            public String asignar(String id, String responsable) {
                Resultado resultado = Gestor.asignarResponsable(id, responsable, ARCHIVO);
                return Servidor.json(resultado.ok, resultado.mensaje, resultado.datos);
            }

            public String buscar(String tipo, String prioridad, String estado) {
                Resultado resultado = Gestor.buscar(ARCHIVO, tipo, prioridad, estado);
                return Servidor.json(resultado.ok, resultado.mensaje, resultado.datos);
            }

            public String reporte() {
                Resultado resultado = Gestor.reporte(ARCHIVO);
                return Servidor.json(resultado.ok, resultado.mensaje, resultado.datos);
            }
        });
    }
}
