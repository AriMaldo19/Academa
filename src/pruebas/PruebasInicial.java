package pruebas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PruebasInicial {

    public static void main(String[] args) throws IOException {

        Path archivoTemporal = Files.createTempFile(
                "academa-prueba-",
                ".txt"
        );

        System.out.println("Archivo de prueba creado:");
        System.out.println(archivoTemporal);

        Files.deleteIfExists(archivoTemporal);

        System.out.println("Archivo temporal eliminado.");
    }
}