
package pruebas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import inicial.Gestor;
import inicial.Resultado;

public class PruebasInicial {

    public static void main(String[] args) throws IOException {

        System.out.println("=== INICIANDO PRUEBAS DE ACADEMA ===");
        System.out.println();

        probarRegistroValido();
        System.out.println();

        probarRegistroIncompleto();
        System.out.println();

        probarCambioEstado();
        System.out.println();

        probarTransicionInvalida();
        System.out.println();

        probarAsignacionResponsable();
        System.out.println();

        probarGeneracionReporte();
        System.out.println();

        System.out.println("=== TODAS LAS PRUEBAS SUPERADAS ===");
    }

    private static void probarRegistroValido() throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-p01-",
                ".txt"
        );

        try {
            Resultado resultado = Gestor.registrar(
                    "Estudiante Prueba",
                    "12345",
                    "Constancia",
                    "Alta",
                    "Solicitud de constancia",
                    archivoTemporal.toString()
            );

            System.out.println("Resultado del registro:");
            System.out.println("¿Registro exitoso? " + resultado.ok);
            System.out.println("Mensaje: " + resultado.mensaje);
            System.out.println("Datos: " + resultado.datos);

            if (!resultado.ok) {
                throw new AssertionError("ERROR: el registro debería ser exitoso");
            }

            if (!"Solicitud registrada".equals(resultado.mensaje)) {
                throw new AssertionError("ERROR: mensaje de registro inesperado");
            }

            if (resultado.datos.size() != 1) {
                throw new AssertionError("ERROR: debería existir una solicitud");
            }

            if (!"Nueva".equals(resultado.datos.get(0).get("estado"))) {
                throw new AssertionError("ERROR: el estado inicial debería ser Nueva");
            }

            System.out.println("PRUEBA P01 SUPERADA: registro válido");

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal P01 eliminado.");
        }
    }

    private static void probarRegistroIncompleto() throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-p02-",
                ".txt"
        );

        try {
            Resultado resultadoInvalido = Gestor.registrar(
                    "",
                    "67890",
                    "Constancia",
                    "Media",
                    "Solicitud sin nombre",
                    archivoTemporal.toString()
            );

            if (resultadoInvalido.ok) {
                throw new AssertionError(
                        "ERROR: no debería aceptar una solicitud sin nombre"
                );
            }

            if (!"Faltan datos de la solicitud".equals(resultadoInvalido.mensaje)) {
                throw new AssertionError(
                        "ERROR: mensaje de validación inesperado"
                );
            }

            Resultado solicitudesGuardadas = Gestor.buscar(
                    archivoTemporal.toString(),
                    "",
                    "",
                    ""
            );

            if (solicitudesGuardadas.datos.size() != 0) {
                throw new AssertionError(
                        "ERROR: no debería haber solicitudes guardadas"
                );
            }

            System.out.println("PRUEBA P02 SUPERADA: solicitud incompleta rechazada");

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal P02 eliminado.");
        }
    }

    private static void probarCambioEstado() throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-p03-",
                ".txt"
        );

        try {
            // Preparar una solicitud para la prueba
            Resultado registro = Gestor.registrar(
                    "Estudiante Prueba",
                    "12345",
                    "Constancia",
                    "Alta",
                    "Solicitud de constancia",
                    archivoTemporal.toString()
            );

            if (!registro.ok || registro.datos.size() != 1) {
                throw new AssertionError(
                        "ERROR: no se pudo preparar la solicitud para P03"
                );
            }

            String id = registro.datos.get(0).get("id");

            // Cambiar el estado de Nueva a En curso
            Resultado cambioEstado = Gestor.cambiarEstado(
                    id,
                    "En curso",
                    archivoTemporal.toString()
            );

            if (!cambioEstado.ok) {
                throw new AssertionError(
                        "ERROR: el cambio de estado debería ser exitoso"
                );
            }

            if (!"Estado actualizado".equals(cambioEstado.mensaje)) {
                throw new AssertionError(
                        "ERROR: mensaje de cambio de estado inesperado"
                );
            }

            if (!"En curso".equals(cambioEstado.datos.get(0).get("estado"))) {
                throw new AssertionError(
                        "ERROR: el estado debería ser En curso"
                );
            }

            // Verificar que el cambio haya quedado guardado
            Resultado solicitudesGuardadas = Gestor.buscar(
                    archivoTemporal.toString(),
                    "",
                    "",
                    ""
            );

            if (solicitudesGuardadas.datos.size() != 1 ||
                    !"En curso".equals(solicitudesGuardadas.datos.get(0).get("estado"))) {
                throw new AssertionError(
                        "ERROR: el cambio de estado no quedó guardado"
                );
            }

            System.out.println(
                    "PRUEBA P03 SUPERADA: cambio de estado Nueva a En curso"
            );

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal P03 eliminado.");
        }
    }

    private static void probarTransicionInvalida() throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-p04-",
                ".txt"
        );

        try {
            // Preparar una solicitud en estado Nueva
            Resultado registro = Gestor.registrar(
                    "Estudiante Prueba",
                    "12345",
                    "Constancia",
                    "Alta",
                    "Solicitud de constancia",
                    archivoTemporal.toString()
            );

            if (!registro.ok || registro.datos.size() != 1) {
                throw new AssertionError(
                        "ERROR: no se pudo preparar la solicitud para P04"
                );
            }

            String id = registro.datos.get(0).get("id");

            // Intentar una transicion no permitida
            Resultado cambioEstado = Gestor.cambiarEstado(
                    id,
                    "Resuelta",
                    archivoTemporal.toString()
            );

            if (cambioEstado.ok) {
                throw new AssertionError(
                        "ERROR: no deberia permitir Nueva a Resuelta"
                );
            }

            if (!"Esa transicion de estado no esta permitida"
                    .equals(cambioEstado.mensaje)) {
                throw new AssertionError(
                        "ERROR: mensaje de transicion inesperado"
                );
            }

            // Comprobar que la solicitud sigue en estado Nueva
            Resultado solicitudesGuardadas = Gestor.buscar(
                    archivoTemporal.toString(),
                    "",
                    "",
                    ""
            );

            if (solicitudesGuardadas.datos.size() != 1 ||
                    !"Nueva".equals(solicitudesGuardadas.datos.get(0).get("estado"))) {
                throw new AssertionError(
                        "ERROR: una transicion invalida modifico el estado"
                );
            }

            System.out.println(
                    "PRUEBA P04 SUPERADA: transicion invalida rechazada"
            );

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal P04 eliminado.");
        }
    }


    private static void probarAsignacionResponsable() throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-p05-",
                ".txt"
        );

        try {
            // Preparar una solicitud para la prueba
            Resultado registro = Gestor.registrar(
                    "Estudiante Prueba",
                    "12345",
                    "Equivalencia",
                    "Media",
                    "Solicitud de equivalencia",
                    archivoTemporal.toString()
            );

            if (!registro.ok || registro.datos.size() != 1) {
                throw new AssertionError(
                        "ERROR: no se pudo preparar la solicitud para P05"
                );
            }

            String id = registro.datos.get(0).get("id");

            // Asignar un responsable
            Resultado asignacion = Gestor.asignarResponsable(
                    id,
                    "Area Alumnos",
                    archivoTemporal.toString()
            );

            if (!asignacion.ok) {
                throw new AssertionError(
                        "ERROR: la asignacion deberia ser exitosa"
                );
            }

            if (!"Responsable asignado".equals(asignacion.mensaje)) {
                throw new AssertionError(
                        "ERROR: mensaje de asignacion inesperado"
                );
            }

            // Verificar que el responsable quedo guardado
            Resultado solicitudesGuardadas = Gestor.buscar(
                    archivoTemporal.toString(),
                    "",
                    "",
                    ""
            );

            if (solicitudesGuardadas.datos.size() != 1 ||
                    !"Area Alumnos".equals(
                            solicitudesGuardadas.datos.get(0).get("responsable"))) {
                throw new AssertionError(
                        "ERROR: el responsable no quedo guardado"
                );
            }

            // Asignar responsable no debe cambiar el estado
            if (!"Nueva".equals(
                    solicitudesGuardadas.datos.get(0).get("estado"))) {
                throw new AssertionError(
                        "ERROR: la asignacion modifico el estado"
                );
            }

            System.out.println(
                    "PRUEBA P05 SUPERADA: responsable asignado correctamente"
            );

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal P05 eliminado.");
        }
    }


    private static void probarGeneracionReporte() throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-p06-",
                ".txt"
        );

        try {
            // Registrar la primera solicitud
            Resultado registro1 = Gestor.registrar(
                    "Ana Perez",
                    "1001",
                    "Constancia",
                    "Alta",
                    "Pedido de constancia",
                    archivoTemporal.toString()
            );

            // Registrar la segunda solicitud
            Resultado registro2 = Gestor.registrar(
                    "Luis Gomez",
                    "1002",
                    "Equivalencia",
                    "Media",
                    "Pedido de equivalencia",
                    archivoTemporal.toString()
            );

            if (!registro1.ok || !registro2.ok) {
                throw new AssertionError(
                        "ERROR: no se pudieron preparar las solicitudes para P06"
                );
            }

            // Generar el reporte
            Resultado reporte = Gestor.reporte(
                    archivoTemporal.toString()
            );

            if (!reporte.ok) {
                throw new AssertionError(
                        "ERROR: la generacion del reporte deberia ser exitosa"
                );
            }

            if (reporte.datos.size() != 2) {
                throw new AssertionError(
                        "ERROR: el reporte deberia contener dos solicitudes"
                );
            }

            // Verificar los datos visibles del reporte
            String contenido = reporte.mensaje;

            if (!contenido.contains("Ana Perez") ||
                    !contenido.contains("Constancia") ||
                    !contenido.contains("Alta") ||
                    !contenido.contains("TRAMITE") ||
                    !contenido.contains("URGENTE")) {
                throw new AssertionError(
                        "ERROR: faltan datos de la primera solicitud en el reporte"
                );
            }

            if (!contenido.contains("Luis Gomez") ||
                    !contenido.contains("Equivalencia") ||
                    !contenido.contains("Media") ||
                    !contenido.contains("ACADEMICO") ||
                    !contenido.contains("NORMAL")) {
                throw new AssertionError(
                        "ERROR: faltan datos de la segunda solicitud en el reporte"
                );
            }

            System.out.println(
                    "PRUEBA P06 SUPERADA: reporte generado correctamente"
            );

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal P06 eliminado.");
        }
    }


}
