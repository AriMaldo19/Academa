package interfaz;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Servidor {

    public interface Api {
        String registrar(String nombre, String legajo, String tipo, String prioridad, String descripcion);

        String cambiarEstado(String id, String estado);

        String asignar(String id, String responsable);

        String buscar(String tipo, String prioridad, String estado);

        String reporte();
    }

    public static void iniciar(int puerto, String version, String titulo, Api api) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", puerto), 0);
        server.createContext("/", intercambio -> atender(intercambio, version, titulo, api));
        server.start();
        String direccion = "http://127.0.0.1:" + puerto;
        System.out.println(titulo);
        System.out.println("Listo en " + direccion);
        System.out.println("Para cerrar esta version, cerra esta ventana.");
        try {
            Desktop.getDesktop().browse(new URI(direccion));
        } catch (Exception ex) {
            System.out.println("Abri esa direccion en el navegador.");
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static String json(boolean ok, String mensaje, List<Map<String, String>> datos) {
        StringBuilder texto = new StringBuilder();
        texto.append("{\"ok\":").append(ok);
        texto.append(",\"mensaje\":").append(comillas(mensaje));
        texto.append(",\"datos\":[");
        for (int i = 0; i < datos.size(); i++) {
            if (i > 0) {
                texto.append(',');
            }
            texto.append('{');
            boolean primero = true;
            for (Map.Entry<String, String> entrada : datos.get(i).entrySet()) {
                if (!primero) {
                    texto.append(',');
                }
                primero = false;
                texto.append(comillas(entrada.getKey())).append(':').append(comillas(entrada.getValue()));
            }
            texto.append('}');
        }
        texto.append("]}");
        return texto.toString();
    }

    private static void atender(HttpExchange intercambio, String version, String titulo, Api api) throws IOException {
        String camino = intercambio.getRequestURI().getPath();
        String metodo = intercambio.getRequestMethod();
        if ("GET".equals(metodo) && ("/".equals(camino) || "/index.html".equals(camino))) {
            responder(intercambio, 200, "text/html; charset=utf-8", Files.readString(Path.of("web", "index.html"), StandardCharsets.UTF_8));
            return;
        }
        if ("GET".equals(metodo) && "/estilos.css".equals(camino)) {
            responder(intercambio, 200, "text/css; charset=utf-8", Files.readString(Path.of("web", "estilos.css"), StandardCharsets.UTF_8));
            return;
        }
        if ("GET".equals(metodo) && "/app.js".equals(camino)) {
            responder(intercambio, 200, "text/javascript; charset=utf-8", Files.readString(Path.of("web", "app.js"), StandardCharsets.UTF_8));
            return;
        }
        if ("GET".equals(metodo) && "/api/info".equals(camino)) {
            responder(intercambio, 200, "application/json; charset=utf-8",
                    "{\"version\":" + comillas(version) + ",\"titulo\":" + comillas(titulo) + "}");
            return;
        }
        if ("POST".equals(metodo) && camino.startsWith("/api/")) {
            Map<String, String> datos = objeto(new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            String respuesta = switch (camino) {
                case "/api/registrar" -> api.registrar(datos.get("nombre"), datos.get("legajo"), datos.get("tipo"), datos.get("prioridad"), datos.get("descripcion"));
                case "/api/estado" -> api.cambiarEstado(datos.get("id"), datos.get("estado"));
                case "/api/responsable" -> api.asignar(datos.get("id"), datos.get("responsable"));
                case "/api/buscar" -> api.buscar(vacio(datos.get("tipo")), vacio(datos.get("prioridad")), vacio(datos.get("estado")));
                case "/api/reporte" -> api.reporte();
                default -> null;
            };
            if (respuesta == null) {
                responder(intercambio, 404, "text/plain; charset=utf-8", "No encontrado");
            } else {
                responder(intercambio, 200, "application/json; charset=utf-8", respuesta);
            }
            return;
        }
        responder(intercambio, 404, "text/plain; charset=utf-8", "No encontrado");
    }

    private static String vacio(String valor) {
        return valor == null ? "" : valor;
    }

    private static void responder(HttpExchange intercambio, int codigo, String tipo, String cuerpo) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().set("Content-Type", tipo);
        intercambio.sendResponseHeaders(codigo, bytes.length);
        intercambio.getResponseBody().write(bytes);
        intercambio.close();
    }

    private static String comillas(String valor) {
        if (valor == null) {
            return "\"\"";
        }
        StringBuilder texto = new StringBuilder("\"");
        for (int i = 0; i < valor.length(); i++) {
            char caracter = valor.charAt(i);
            switch (caracter) {
                case '"' -> texto.append("\\\"");
                case '\\' -> texto.append("\\\\");
                case '\n' -> texto.append("\\n");
                case '\r' -> texto.append("\\r");
                case '\t' -> texto.append("\\t");
                default -> {
                    if (caracter < 32) {
                        texto.append(String.format("\\u%04x", (int) caracter));
                    } else {
                        texto.append(caracter);
                    }
                }
            }
        }
        texto.append('"');
        return texto.toString();
    }

    private static Map<String, String> objeto(String json) {
        Map<String, String> mapa = new LinkedHashMap<>();
        int i = 0;
        while (i < json.length()) {
            int abre = json.indexOf('"', i);
            if (abre < 0) {
                break;
            }
            int cierraClave = finDeCadena(json, abre + 1);
            if (cierraClave < 0) {
                break;
            }
            String clave = desescapar(json.substring(abre + 1, cierraClave));
            int dosPuntos = json.indexOf(':', cierraClave);
            if (dosPuntos < 0) {
                break;
            }
            int abreValor = json.indexOf('"', dosPuntos);
            if (abreValor < 0) {
                break;
            }
            int cierraValor = finDeCadena(json, abreValor + 1);
            if (cierraValor < 0) {
                break;
            }
            mapa.put(clave, desescapar(json.substring(abreValor + 1, cierraValor)));
            i = cierraValor + 1;
        }
        return mapa;
    }

    private static int finDeCadena(String json, int desde) {
        boolean escape = false;
        for (int i = desde; i < json.length(); i++) {
            char caracter = json.charAt(i);
            if (escape) {
                escape = false;
            } else if (caracter == '\\') {
                escape = true;
            } else if (caracter == '"') {
                return i;
            }
        }
        return -1;
    }

    private static String desescapar(String texto) {
        StringBuilder salida = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (!escape) {
                if (caracter == '\\') {
                    escape = true;
                } else {
                    salida.append(caracter);
                }
                continue;
            }
            escape = false;
            switch (caracter) {
                case 'n' -> salida.append('\n');
                case 'r' -> salida.append('\r');
                case 't' -> salida.append('\t');
                default -> salida.append(caracter);
            }
        }
        return salida.toString();
    }
}
