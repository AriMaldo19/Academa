package inicial;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Version inicial, a proposito desordenada.
// El comportamiento visible es el que las pruebas comparan con la version refactorizada.

public class Gestor {

    public Resultado procesar(String n, String l, String t, String p, String d, String ruta) {
        String aux = "";
        int x = 0;
        try {
            n = n == null ? "" : n.trim();
            l = l == null ? "" : l.trim();
            t = t == null ? "" : t.trim();
            p = p == null ? "" : p.trim();
            d = d == null ? "" : d.trim();
            if (!n.equals("") && !l.equals("") && !t.equals("") && !p.equals("") && !d.equals("")) {
                if (t.equals("Constancia") || t.equals("Equivalencia") || t.equals("Revision")) {
                    if (p.equals("Alta") || p.equals("Media") || p.equals("Baja")) {
                        List<String> lineas = new ArrayList<>();
                        try {
                            lineas = new ArrayList<>(Files.readAllLines(Path.of(ruta), StandardCharsets.UTF_8));
                        } catch (Exception ex) {
                        }
                        x = 1;
                        for (String s : lineas) {
                            String[] partes = s.split("\\|", -1);
                            if (partes.length > 0 && partes[0].matches("\\d+")) {
                                if (Integer.parseInt(partes[0]) >= x) {
                                    x = Integer.parseInt(partes[0]) + 1;
                                }
                            }
                        }
                        String etiqueta = "";
                        if (t.equals("Constancia")) {
                            etiqueta = "TRAMITE";
                        } else if (t.equals("Equivalencia")) {
                            etiqueta = "ACADEMICO";
                        } else if (t.equals("Revision")) {
                            etiqueta = "EVALUACION";
                        } else {
                            etiqueta = "";
                        }
                        String marca = "";
                        if (p.equals("Alta")) {
                            marca = "URGENTE";
                        } else if (p.equals("Media")) {
                            marca = "NORMAL";
                        } else if (p.equals("Baja")) {
                            marca = "BAJA";
                        } else {
                            marca = "";
                        }
                        String fila = x + "|" + n + "|" + l + "|" + t + "|" + p + "|" + d + "|Nueva|-|" + etiqueta + "|" + marca;
                        lineas.add(fila);
                        try {
                            StringBuilder contenido = new StringBuilder();
                            for (String item : lineas) {
                                contenido.append(item).append("\n");
                            }
                            Files.writeString(Path.of(ruta), contenido.toString(), StandardCharsets.UTF_8);
                        } catch (Exception ex) {
                        }
                        String texto = "";
                        for (String s : lineas) {
                            String[] partes = s.split("\\|", -1);
                            if (partes.length >= 10) {
                                texto = texto + partes[0] + " | " + partes[1] + " | " + partes[3] + " | " + partes[4] + " | " + partes[6] + " | " + partes[7] + " | " + partes[8] + " | " + partes[9] + "\n";
                            }
                        }
                        aux = "Solicitud registrada";
                        return new Resultado(true, aux, aDiccionarios(lineas));
                    } else {
                        return Resultado.fallo("Prioridad no valida");
                    }
                } else {
                    return Resultado.fallo("Tipo de solicitud no valido");
                }
            } else {
                return Resultado.fallo("Faltan datos de la solicitud");
            }
        } catch (Exception ex) {
            return Resultado.fallo("Faltan datos de la solicitud");
        }
    }

    public Resultado cambiar(String id, String e, String ruta) {
        List<String> lineas;
        try {
            lineas = new ArrayList<>(Files.readAllLines(Path.of(ruta), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            lineas = new ArrayList<>();
        }
        List<String> nueva = new ArrayList<>();
        boolean encontrada = false;
        String mensaje = "No se encontro la solicitud";
        boolean ok = false;
        for (String s : lineas) {
            String[] partes = s.split("\\|", -1);
            if (partes.length >= 10 && partes[0].equals(String.valueOf(id))) {
                encontrada = true;
                String actual = partes[6];
                boolean permitido = false;
                if (e.equals("En curso")) {
                    if (actual.equals("Nueva")) {
                        permitido = true;
                    }
                } else if (e.equals("Resuelta")) {
                    if (actual.equals("En curso")) {
                        permitido = true;
                    }
                } else if (e.equals("Rechazada")) {
                    if (actual.equals("En curso")) {
                        permitido = true;
                    }
                }
                if (permitido == false) {
                    mensaje = "Esa transicion de estado no esta permitida";
                    ok = false;
                    nueva.add(s);
                } else {
                    partes[6] = e;
                    nueva.add(String.join("|", partes));
                    mensaje = "Estado actualizado";
                    ok = true;
                }
            } else {
                if (!s.trim().equals("")) {
                    nueva.add(s);
                }
            }
        }
        if (encontrada) {
            try {
                StringBuilder contenido = new StringBuilder();
                for (String item : nueva) {
                    contenido.append(item).append("\n");
                }
                Files.writeString(Path.of(ruta), contenido.toString(), StandardCharsets.UTF_8);
            } catch (Exception ex) {
            }
        }
        return new Resultado(ok, mensaje, aDiccionarios(encontrada ? nueva : lineas));
    }

    public Resultado asignar(String id, String r, String ruta) {
        if (r == null || r.trim().equals("")) {
            return Resultado.fallo("El responsable no puede estar vacio");
        }
        List<String> lineas;
        try {
            lineas = new ArrayList<>(Files.readAllLines(Path.of(ruta), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            lineas = new ArrayList<>();
        }
        List<String> nueva = new ArrayList<>();
        boolean encontrada = false;
        for (String s : lineas) {
            String[] partes = s.split("\\|", -1);
            if (partes.length >= 10 && partes[0].equals(String.valueOf(id))) {
                encontrada = true;
                partes[7] = r.trim();
                nueva.add(String.join("|", partes));
            } else {
                if (!s.trim().equals("")) {
                    nueva.add(s);
                }
            }
        }
        if (!encontrada) {
            return new Resultado(false, "No se encontro la solicitud", aDiccionarios(lineas));
        }
        try {
            StringBuilder contenido = new StringBuilder();
            for (String item : nueva) {
                contenido.append(item).append("\n");
            }
            Files.writeString(Path.of(ruta), contenido.toString(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
        }
        return new Resultado(true, "Responsable asignado", aDiccionarios(nueva));
    }

    public Resultado filtrar(String ruta, String t, String p, String e) {
        List<String> lineas;
        try {
            lineas = new ArrayList<>(Files.readAllLines(Path.of(ruta), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            lineas = new ArrayList<>();
        }
        List<String> salida = new ArrayList<>();
        for (String s : lineas) {
            String[] partes = s.split("\\|", -1);
            if (partes.length < 10) {
                continue;
            }
            boolean sirve = true;
            if (t != null && !t.equals("") && !partes[3].equals(t)) {
                sirve = false;
            }
            if (p != null && !p.equals("") && !partes[4].equals(p)) {
                sirve = false;
            }
            if (e != null && !e.equals("") && !partes[6].equals(e)) {
                sirve = false;
            }
            if (sirve == true) {
                salida.add(s);
            }
        }
        return new Resultado(true, "", aDiccionarios(salida));
    }

    public Resultado armarReporte(String ruta) {
        List<String> lineas;
        try {
            lineas = new ArrayList<>(Files.readAllLines(Path.of(ruta), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            lineas = new ArrayList<>();
        }
        String texto = "";
        for (String s : lineas) {
            String[] partes = s.split("\\|", -1);
            if (partes.length >= 10) {
                String etiqueta;
                if (partes[2].equals("Constancia")) {
                    etiqueta = "TRAMITE";
                } else if (partes[2].equals("Equivalencia")) {
                    etiqueta = "ACADEMICO";
                } else if (partes[2].equals("Revision")) {
                    etiqueta = "EVALUACION";
                } else {
                    etiqueta = partes[8];
                }
                texto = texto + partes[0] + " | " + partes[1] + " | " + partes[3] + " | " + partes[4] + " | " + partes[6] + " | " + partes[7] + " | " + partes[8] + " | " + partes[9] + "\n";
            }
        }
        return new Resultado(true, texto, aDiccionarios(lineas));
    }

    private List<Map<String, String>> aDiccionarios(List<String> lineas) {
        List<Map<String, String>> datos = new ArrayList<>();
        for (String s : lineas) {
            String[] partes = s.split("\\|", -1);
            if (partes.length < 10) {
                continue;
            }
            Map<String, String> fila = new LinkedHashMap<>();
            fila.put("id", partes[0]);
            fila.put("nombre", partes[1]);
            fila.put("legajo", partes[2]);
            fila.put("tipo", partes[3]);
            fila.put("prioridad", partes[4]);
            fila.put("descripcion", partes[5]);
            fila.put("estado", partes[6]);
            fila.put("responsable", partes[7]);
            datos.add(fila);
        }
        return datos;
    }

    // Quedo de una prueba anterior y nadie lo llama.
    public int calcular(String a, int b, int c) {
        if (a.equals("ALTA")) {
            return b + c;
        } else if (a.equals("MEDIA")) {
            return b;
        } else {
            return 0;
        }
    }

    public static Resultado registrar(String nombre, String legajo, String tipo, String prioridad, String descripcion, String ruta) {
        return new Gestor().procesar(nombre, legajo, tipo, prioridad, descripcion, ruta);
    }

    public static Resultado cambiarEstado(String identificador, String nuevoEstado, String ruta) {
        return new Gestor().cambiar(identificador, nuevoEstado, ruta);
    }

    public static Resultado asignarResponsable(String identificador, String responsable, String ruta) {
        return new Gestor().asignar(identificador, responsable, ruta);
    }

    public static Resultado buscar(String ruta, String tipo, String prioridad, String estado) {
        return new Gestor().filtrar(ruta, tipo, prioridad, estado);
    }

    public static Resultado reporte(String ruta) {
        return new Gestor().armarReporte(ruta);
    }
}
