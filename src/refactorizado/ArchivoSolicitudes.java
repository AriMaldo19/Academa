package refactorizado;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Único lugar que lee y escribe el archivo de solicitudes.
// Formato: id|nombre|legajo|tipo|prioridad|descripcion|estado|responsable|etiqueta|marca
public class ArchivoSolicitudes {

    private static final String SEPARADOR = "|";
    private static final int CANTIDAD_CAMPOS = 10;

    private final Path ruta;

    public ArchivoSolicitudes(String ruta) {
        this.ruta = Path.of(ruta);
    }

    // Si el archivo todavía no existe, no hay solicitudes guardadas.
    public List<Solicitud> leer() {
        List<String> lineas;
        try {
            lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
        } catch (NoSuchFileException ex) {
            return new ArrayList<>();
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el archivo de solicitudes", ex);
        }
        List<Solicitud> solicitudes = new ArrayList<>();
        for (String linea : lineas) {
            String[] campos = linea.split("\\" + SEPARADOR, -1);
            if (campos.length >= CANTIDAD_CAMPOS) {
                solicitudes.add(desdeCampos(campos));
            }
        }
        return solicitudes;
    }

    public void guardar(List<Solicitud> solicitudes) {
        StringBuilder contenido = new StringBuilder();
        for (Solicitud solicitud : solicitudes) {
            contenido.append(aLinea(solicitud)).append("\n");
        }
        try {
            Files.writeString(ruta, contenido.toString(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar el archivo de solicitudes", ex);
        }
    }

    private Solicitud desdeCampos(String[] campos) {
        return new Solicitud(
                campos[0],
                new Estudiante(campos[1], campos[2]),
                campos[3],
                campos[4],
                campos[5],
                EstadoSolicitud.crear(campos[6]),
                new Responsable(campos[7]),
                campos[8],
                campos[9]);
    }

    private String aLinea(Solicitud solicitud) {
        return String.join(SEPARADOR,
                solicitud.id(),
                solicitud.estudiante().nombre(),
                solicitud.estudiante().legajo(),
                solicitud.tipo(),
                solicitud.prioridad(),
                solicitud.descripcion(),
                solicitud.nombreEstado(),
                solicitud.responsable().nombre(),
                solicitud.etiqueta(),
                solicitud.marca());
    }
}
