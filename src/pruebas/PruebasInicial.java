package pruebas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import inicial.Gestor;
import inicial.Resultado;

public class PruebasInicial {

    public static void main(String[] args) throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-prueba-",
                ".txt"
        );

        System.out.println("Archivo de prueba creado:");
        System.out.println(archivoTemporal);

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

            Resultado resultadoInvalido = Gestor.registrar(
                    "",
                    "67890",
                    "Constancia",
                    "Media",
                    "Solicitud sin nombre",
                    archivoTemporal.toString()
            );

            if (resultadoInvalido.ok) {
                throw new AssertionError("ERROR: no debería aceptar una solicitud sin nombre");
            }

            if (!"Faltan datos de la solicitud".equals(resultadoInvalido.mensaje)) {
                throw new AssertionError("ERROR: mensaje de validación inesperado");
            }

            Resultado solicitudesGuardadas = Gestor.buscar(
                    archivoTemporal.toString(),
                    "",
                    "",
                    ""
            );

            if (solicitudesGuardadas.datos.size() != 1) {
                throw new AssertionError(
                        "ERROR: la solicitud incompleta no debería haberse guardado"
                );
            }

            System.out.println("PRUEBA P02 SUPERADA: solicitud incompleta rechazada");

            Resultado cambioEstado = Gestor.cambiarEstado(
                    "1",
                    "En curso",
                    archivoTemporal.toString()
            );

            if (!cambioEstado.ok) {
                throw new AssertionError("ERROR: el cambio de estado debería ser exitoso");
            }

            if (!"Estado actualizado".equals(cambioEstado.mensaje)) {
                throw new AssertionError("ERROR: mensaje de cambio de estado inesperado");
            }

            if (!"En curso".equals(cambioEstado.datos.get(0).get("estado"))) {
                throw new AssertionError("ERROR: el estado debería ser En curso");
            }

            System.out.println("PRUEBA P03 SUPERADA: cambio de estado Nueva a En curso");

        } finally {
            Files.deleteIfExists(archivoTemporal);
            System.out.println("Archivo temporal eliminado.");
        }
    }
}