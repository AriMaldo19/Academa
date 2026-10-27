package pruebas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Comparacion {
    private static int operaciones = 0;
    private static Path rutaInicial;
    private static Path rutaRefactorizada;
    private static refactorizado.Gestor gestorRefactorizado;

    public static void main(String[] args) throws IOException {
        rutaInicial = Files.createTempFile("academa-comparacion-inicial-", ".txt");
        rutaRefactorizada = Files.createTempFile("academa-comparacion-refactor-", ".txt");
        gestorRefactorizado = new refactorizado.Gestor(
                new refactorizado.ArchivoSolicitudes(rutaRefactorizada.toString()));
        try {
            System.out.println("=== COMPARACION INICIAL / REFACTORIZADA ===");

            registrar("Ana Perez", "1001", "Constancia", "Alta", "Pedido de constancia", true, "Solicitud registrada");
            registrar("", "1002", "Equivalencia", "Media", "Pedido incompleto", false, "Faltan datos de la solicitud");
            registrar("Luis Gomez", "1002", "Otro", "Baja", "Pedido invalido", false, "Tipo de solicitud no valido");
            registrar("Luis Gomez", "1002", "Equivalencia", "Urgente", "Pedido invalido", false, "Prioridad no valida");
            registrar("Luis Gomez", "1002", "Equivalencia", "Media", "Pedido de equivalencia", true, "Solicitud registrada");
            registrar("Mora Diaz", "1003", "Revision", "Baja", "Revision de parcial", true, "Solicitud registrada");

            cambiar("1", "En curso", true, "Estado actualizado");
            cambiar("1", "Nueva", false, "Esa transicion de estado no esta permitida");
            cambiar("2", "Resuelta", false, "Esa transicion de estado no esta permitida");
            cambiar("1", "Resuelta", true, "Estado actualizado");
            cambiar("1", "En curso", false, "Esa transicion de estado no esta permitida");
            cambiar("99", "En curso", false, "No se encontro la solicitud");

            asignar("2", "Area Alumnos", true, "Responsable asignado");
            asignar("2", "", false, "El responsable no puede estar vacio");
            asignar("50", "Mesa de entrada", false, "No se encontro la solicitud");

            buscar("Equivalencia", "", "", List.of("2"));
            buscar("Constancia", "Baja", "", List.of());

            comparar("Reporte", inicial.Gestor.reporte(rutaInicial.toString()), gestorRefactorizado.reporte(),
                    true, null, false);
            String reporte = inicial.Gestor.reporte(rutaInicial.toString()).mensaje;
            for (String valor : List.of("Ana Perez", "Resuelta", "Luis Gomez", "Area Alumnos",
                    "Mora Diaz", "TRAMITE", "ACADEMICO", "EVALUACION", "URGENTE", "NORMAL", "BAJA")) {
                if (!reporte.contains(valor)) throw new AssertionError("Reporte incompleto: falta " + valor);
            }
            Path inexistenteInicial = Files.createTempFile("academa-inexistente-inicial-", ".txt");
            Path inexistenteRefactor = Files.createTempFile("academa-inexistente-refactor-", ".txt");
            try {
                Files.delete(inexistenteInicial);
                Files.delete(inexistenteRefactor);
                refactorizado.Gestor otroGestor = new refactorizado.Gestor(
                        new refactorizado.ArchivoSolicitudes(inexistenteRefactor.toString()));
                comparar("Archivo inexistente", inicial.Gestor.buscar(inexistenteInicial.toString(), "", "", ""),
                        otroGestor.buscar("", "", ""), true, "", false);
            } finally {
                Files.deleteIfExists(inexistenteInicial);
                Files.deleteIfExists(inexistenteRefactor);
            }
            System.out.println("OK: las dos versiones coinciden en " + operaciones
                    + " operaciones, en la generación de reportes de solicitudes\n" +
                    "y en el manejo de archivos inexistentes.");
        } finally {
            Files.deleteIfExists(rutaInicial);
            Files.deleteIfExists(rutaRefactorizada);
        }
    }

    private static void registrar(String nombre, String legajo, String tipo, String prioridad,
                                 String descripcion, boolean esperado, String mensaje) {
        inicial.Resultado a = inicial.Gestor.registrar(nombre, legajo, tipo, prioridad, descripcion,
                rutaInicial.toString());
        refactorizado.Resultado b = gestorRefactorizado.registrar(
                new refactorizado.DatosSolicitud(nombre, legajo, tipo, prioridad, descripcion));
        compararOperacion("Registrar " + tipo, a, b, esperado, mensaje);
    }

    private static void cambiar(String id, String estado, boolean esperado, String mensaje) {
        compararOperacion("Cambiar " + id + " -> " + estado,
                inicial.Gestor.cambiarEstado(id, estado, rutaInicial.toString()),
                gestorRefactorizado.cambiarEstado(id, estado), esperado, mensaje);
    }

    private static void asignar(String id, String responsable, boolean esperado, String mensaje) {
        compararOperacion("Asignar " + id + " -> " + responsable,
                inicial.Gestor.asignarResponsable(id, responsable, rutaInicial.toString()),
                gestorRefactorizado.asignarResponsable(id, responsable), esperado, mensaje);
    }

    private static void buscar(String tipo, String prioridad, String estado, List<String> idsEsperados) {
        inicial.Resultado a = inicial.Gestor.buscar(rutaInicial.toString(), tipo, prioridad, estado);
        refactorizado.Resultado b = gestorRefactorizado.buscar(tipo, prioridad, estado);
        compararOperacion("Buscar " + tipo + "/" + prioridad + "/" + estado, a, b, true, "");
        List<String> idsReales = new ArrayList<>();
        for (Map<String, String> dato : a.datos) idsReales.add(dato.get("id"));
        if (!idsReales.equals(idsEsperados)) {
            throw new AssertionError("Busqueda: IDs esperados " + idsEsperados + ", obtenidos " + idsReales);
        }
    }

    private static void compararOperacion(String paso, inicial.Resultado a, refactorizado.Resultado b,
                                         boolean esperado, String mensaje) {
        comparar(paso, a, b, esperado, mensaje, true);
    }

    private static void comparar(String paso, inicial.Resultado a, refactorizado.Resultado b,
                                 boolean esperado, String mensaje, boolean contarOperacion) {
        if (a.ok != esperado || b.ok != esperado || (mensaje != null &&
                (!Objects.equals(a.mensaje, mensaje) || !Objects.equals(b.mensaje, mensaje)))) {
            throw new AssertionError(paso + " - respuesta inesperada\nInicial: " + firma(a)
                    + "\nRefactorizada: " + firma(b));
        }
        if (a.ok != b.ok || !Objects.equals(a.mensaje, b.mensaje)
                || !Objects.equals(a.datos, b.datos)) {
            throw new AssertionError(paso + " - DIFERENCIA\nInicial: " + firma(a)
                    + "\nRefactorizada: " + firma(b));
        }
        if (contarOperacion) operaciones++;
        System.out.println("OK " + (contarOperacion ? String.format("%02d", operaciones) + " " : "") + paso);
    }

    private static String firma(inicial.Resultado r) {
        return "ok=" + r.ok + ", mensaje=" + r.mensaje + ", datos=" + r.datos;
    }
    private static String firma(refactorizado.Resultado r) {
        return "ok=" + r.ok + ", mensaje=" + r.mensaje + ", datos=" + r.datos;
    }
}
