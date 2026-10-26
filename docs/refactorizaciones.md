# Refactorizaciones aplicadas

Este documento registra los cambios realizados para pasar de la versión inicial (`src/inicial/`) a la versión refactorizada (`src/refactorizado/`). Ambas versiones mantienen el mismo comportamiento visible: los mismos mensajes, los mismos datos devueltos y el mismo formato del archivo de solicitudes. Las referencias a líneas corresponden a `src/inicial/Gestor.java`, igual que en [Diagnostico.md](Diagnostico.md).

---

## 1. Estructura de la versión refactorizada

| Clase | Responsabilidad |
|---|---|
| `Main` | Inicia el servidor en el puerto 8082, usa `solicitudes-refactorizada.txt` y traduce los errores del archivo en una respuesta con `ok = false`. |
| `Gestor` | Coordina las operaciones (`registrar`, `cambiarEstado`, `asignarResponsable`, `buscar`, `reporte`) y delega las reglas. |
| `ArchivoSolicitudes` | Único punto de lectura y escritura del archivo de texto. |
| `DatosSolicitud` | Agrupa los datos que llegan al registrar y valida que estén completos (`tieneDatosMinimos`). |
| `Estudiante` | Nombre y legajo de quien presenta la solicitud. |
| `Responsable` | Responsable asignado; `SIN_ASIGNAR` representa el valor `-`. |
| `Solicitud` | Datos y comportamiento del trámite: cambio de estado, asignación, filtro, renglón del reporte y marca de prioridad (`marcaDe`). |
| `TipoSolicitud` + `Constancia`, `Equivalencia`, `Revision` | Strategy: cada tipo define su etiqueta. |
| `EstadoSolicitud` + `Nueva`, `EnCurso`, `Resuelta`, `Rechazada` | State: cada estado define a qué estado puede pasar. |
| `Resultado` | Respuesta de cada operación (`ok`, `mensaje`, `datos`), igual que en la versión inicial. |

---

## 2. Mitigación de los nueve code smells

| # | Code smell | Versión inicial | Refactoring aplicado | Versión refactorizada |
|---|---|---|---|---|
| 1 | Long Method | `procesar()`, líneas 16-93 | Extract Method, Move Method | `Gestor.registrar()` queda en pocas líneas y delega en `tieneDatosMinimos`, `TipoSolicitud.crear`, `Solicitud.esPrioridadValida`, `Solicitud.nueva`, `siguienteId` y `ArchivoSolicitudes`. Se eliminó el texto que se armaba y no se usaba (líneas 72-78). |
| 2 | Long Parameter List | `procesar(n, l, t, p, d, ruta)`, línea 16 | Introduce Parameter Object | `registrar(DatosSolicitud datos)`. La ruta pasa a ser estado de `ArchivoSolicitudes`. |
| 3 | Mysterious Name | `n`, `l`, `t`, `p`, `d`, `aux`, `x`, `s`, `e`, `r` | Rename Variable / Rename Method | `nombre`, `legajo`, `tipo`, `prioridad`, `descripcion`, `nuevoEstado`, `nombreResponsable`, `siguienteId`, `solicitud`. Los métodos `procesar`, `cambiar`, `asignar`, `filtrar` y `armarReporte` pasan a llamarse como la operación que realizan. |
| 4 | God Class | Clase `Gestor` completa | Extract Class, Move Method | `Gestor` solo coordina; la persistencia, las reglas de tipo, estado y prioridad y la conversión de datos viven en sus propias clases. |
| 5 | Duplicated Code | Lecturas en 30, 98, 160, 195, 225; escrituras en 69, 147, 186 | Extract Class | `ArchivoSolicitudes.leer()` y `ArchivoSolicitudes.guardar()` son los únicos accesos al archivo. El parseo de cada línea se hace en un solo lugar (`desdeCampos`). |
| 6 | Condicional largo | Líneas 43-60 y 112-124 | Replace Conditional with Strategy / State | Etiqueta: `TipoSolicitud.etiqueta()`. Transiciones: `EstadoSolicitud.puedePasarA()`. Marca: `Solicitud.marcaDe()` (un `switch` breve, ver 4.4). |
| 7 | Excepción escondida | `catch` vacíos en 31, 70, 99, 148, 161, 187, 196, 226 y `catch` genérico en 90 | Replace Error Code with Exception | Archivo inexistente → lista vacía (`NoSuchFileException`). Cualquier otro error → `IllegalStateException` con mensaje descriptivo, que `Main` informa al usuario. Se eliminó el `catch (Exception)` que convertía cualquier error en "Faltan datos de la solicitud". |
| 8 | Dead Code | `calcular()`, líneas 271-279 | Remove Dead Code | No existe en la versión refactorizada. |
| 9 | Regla en el lugar incorrecto | `armarReporte()`, líneas 234-243 | Remove Dead Code, Move Method | `Solicitud.lineaReporte()` usa la etiqueta y la marca guardadas al registrar. Ya no se evalúa `partes[2]` (el legajo). |

---

## 3. Refactorings documentados

### 3.1 Extract Method

**Antes** (`procesar`, línea 25): la validación estaba mezclada con el resto del registro.

```java
if (!n.equals("") && !l.equals("") && !t.equals("") && !p.equals("") && !d.equals("")) {
```

**Después** (`DatosSolicitud.tieneDatosMinimos`, usado por `Gestor.registrar`):

```java
public boolean tieneDatosMinimos() {
    return !estudiante.nombre().isEmpty()
            && !estudiante.legajo().isEmpty()
            && !tipo.isEmpty()
            && !prioridad.isEmpty()
            && !descripcion.isEmpty();
}
```

```java
if (!datos.tieneDatosMinimos()) {
    return Resultado.fallo("Faltan datos de la solicitud");
}
```

También se extrajeron `siguienteId`, `buscarPorId` y `aDatos` en `Gestor`, y `cumpleFiltro` en `Solicitud`.

### 3.2 Move Method

**Antes** (`cambiar`, líneas 112-124): las transiciones se decidían en `Gestor`.

```java
if (e.equals("En curso")) {
    if (actual.equals("Nueva")) {
        permitido = true;
    }
} else if (e.equals("Resuelta")) {
    ...
```

**Después**: cada estado decide sus transiciones y `Solicitud` delega en su estado actual.

```java
// Nueva
public boolean puedePasarA(String destino) {
    return "En curso".equals(destino);
}

// Solicitud
public boolean puedeCambiarA(String nuevoEstado) {
    return estado.puedePasarA(nuevoEstado);
}
```

Con la misma técnica, la etiqueta pasó a `TipoSolicitud`, la lectura y escritura a `ArchivoSolicitudes`, y la conversión a mapas (`aDiccionarios`) a `Solicitud.aMapa()`.

### 3.3 Introduce Parameter Object

**Antes** (línea 16):

```java
public Resultado procesar(String n, String l, String t, String p, String d, String ruta)
```

**Después**:

```java
public DatosSolicitud(String nombre, String legajo, String tipo, String prioridad, String descripcion) {
    this.estudiante = new Estudiante(limpiar(nombre), limpiar(legajo));
    this.tipo = limpiar(tipo);
    this.prioridad = limpiar(prioridad);
    this.descripcion = limpiar(descripcion);
}

public Resultado registrar(DatosSolicitud datos)
```

El responsable no forma parte de `DatosSolicitud` porque se asigna después del registro. La ruta tampoco: pertenece a la persistencia y se pasa una sola vez al construir `ArchivoSolicitudes`.

### 3.4 Replace Conditional with Strategy y State

**Strategy** para los tipos:

```java
public interface TipoSolicitud {
    String etiqueta();

    static TipoSolicitud crear(String nombre) {
        return switch (nombre) {
            case "Constancia" -> new Constancia();
            case "Equivalencia" -> new Equivalencia();
            case "Revision" -> new Revision();
            default -> null;
        };
    }
}
```

**State** para los estados: `EstadoSolicitud.puedePasarA` devuelve `false` por defecto; `Nueva` y `EnCurso` lo redefinen y `Resuelta` y `Rechazada` heredan la negativa. Un estado desconocido leído del archivo se conserva con su nombre y no admite transiciones, igual que en la versión inicial.

---

## 4. SOLID y Clean Code

### 4.1 Principios aplicados

- **SRP:** `ArchivoSolicitudes` (persistencia), `EstadoSolicitud` (transiciones), `TipoSolicitud` (etiqueta), `Solicitud` (datos y comportamiento del trámite), `Gestor` (coordinación), `Main` (conexión con la interfaz web).
- **OCP:** un tipo o estado nuevo se agrega con una clase nueva y una línea en `crear`. `Gestor.registrar` y `Gestor.cambiarEstado` no cambian.

### 4.2 Principios revisados sin cambios específicos

- **LSP:** todo el código consulta `EstadoSolicitud` y `TipoSolicitud` sin depender de la clase concreta.
- **ISP:** `TipoSolicitud` solo declara `etiqueta`; `EstadoSolicitud` solo `nombre` y `puedePasarA`.
- **DIP:** `Gestor` depende de la clase concreta `ArchivoSolicitudes`. Se decidió no crear una interfaz de repositorio porque hay un único mecanismo de persistencia. `Gestor` la recibe por constructor, lo que facilita reemplazarla más adelante.

### 4.3 Clean Code y manejo de errores

- Nombres que expresan intención y métodos cortos con una sola tarea.
- Constantes con nombre (`NO_ENCONTRADA`, `SIN_ASIGNAR`, `CANTIDAD_CAMPOS`, `SEPARADOR`).
- `ArchivoSolicitudes.leer` devuelve una lista vacía solo si el archivo no existe. Ante otro error lanza `IllegalStateException("No se pudo leer el archivo de solicitudes")`; al guardar, `"No se pudo guardar el archivo de solicitudes"`.
- `refactorizado.Main` captura esa excepción y responde `{ok: false, mensaje: <causa>}`, de modo que la interfaz muestra el error real en lugar de una lista vacía o de un fallo de conexión. `Servidor.java` no se modificó.

### 4.4 Decisiones de diseño

- La marca de prioridad queda en `Solicitud.marcaDe` (un `switch` de tres valores). No se creó otra jerarquía de clases porque no hay comportamiento que varíe además del texto.
- No se valida el carácter `|` en la descripción porque la versión inicial tampoco lo hace y eso cambiaría el comportamiento visible.
- El archivo solo se guarda cuando la operación modifica algo. En la versión inicial también se reescribía al rechazar una transición; la diferencia no es visible en las respuestas.

---

## 5. Contraste con el informe (`Informe_Academa_Optimizado.docx`)

**Coincide con el informe:** clases y responsabilidades de las secciones 2.5, 5 y 6 (`Gestor`, `ArchivoSolicitudes`, `DatosSolicitud`, `Estudiante`, `Responsable`, `Solicitud`, `TipoSolicitud`, `EstadoSolicitud` y sus subclases); métodos `tieneDatosMinimos`, `puedePasarA`, `puedeCambiarA`, `etiqueta`, `marcaDe`, `lineaReporte`, `crear`; puerto 8082; mensajes de `IllegalStateException`; los 17 pasos de la sección 7.2 y el reporte de la sección 7.3 (verificados con el script).

**Diferencias a corregir en el informe:**

| Sección | Dice el informe | Implementación | Sugerencia |
|---|---|---|---|
| 5.3 y 8 | La `IllegalStateException` se lanza y no se indica quién la trata. | `refactorizado.Main` la captura y responde `ok = false` con el mensaje. | Agregar una oración indicando que `Main` informa el error al usuario. |
| 7.2 | Mensajes con tilde: "Esa transición de estado no está permitida", "El responsable no puede estar vacío", "No se encontró la solicitud". | Los mensajes reales no llevan tilde (son los de la versión inicial). | Copiar los mensajes tal cual los devuelve el sistema. |
| 2.5, 7.2 y 7.4 | `src/pruebas/Comparacion.java` y `compilar-y-probar.bat`. | No existen en esta rama; la comparación se hace con `verificar-refactorizacion.sh`. | Mencionar el script bash o crear esas clases en la etapa de pruebas. |
| 7.3 | "Se ejecutó con OpenJDK 21.0.12". | Verificado con OpenJDK 17.0.15. | Actualizar la versión usada. |
| 6.4 | `class Constancia implements TipoSolicitud` (sin `public`). | Las clases son `public`, cada una en su archivo. | Opcional; no cambia el diseño. |

---

## 6. Cómo verificar (macOS / Linux)

Desde la raíz del proyecto:

```sh
bash verificar-refactorizacion.sh
```

El script:

1. Compila `interfaz`, `inicial` y `refactorizado` en un directorio temporal (no crea `out/` ni archivos `.txt` en el repositorio).
2. Levanta ambas versiones (8081 y 8082) sin abrir el navegador.
3. Comprueba el reporte con archivo inexistente, ejecuta las 17 operaciones de la sección 7.2 en las dos versiones y compara las respuestas JSON completas y el mensaje esperado.
4. Compara el reporte final con la tabla de la sección 7.3.
5. Verifica que la refactorizada informa un archivo ilegible (smell 7).
6. Hace chequeos estáticos: sin `catch` vacíos ni genéricos, sin `calcular`, acceso a archivos solo en `ArchivoSolicitudes`, sin `partes[2]` y sin cadenas de `if` por tipo.

Termina con `OK: las dos versiones coinciden en 17 operaciones y en el reporte.` y código de salida 0, o informa cada diferencia y termina con código 1.

Para ejecutar solo la versión refactorizada en macOS:

```sh
javac --add-modules jdk.httpserver -encoding UTF-8 -d out src/interfaz/*.java src/refactorizado/*.java
java --add-modules jdk.httpserver -cp out refactorizado.Main   # http://127.0.0.1:8082
```

En Windows: `ejecutar-refactorizada.bat`.
