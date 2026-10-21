# Diagnóstico de deuda técnica y code smells

El análisis de la versión inicial de Academa permitió identificar diferentes problemas de calidad interna que, aunque no impiden el funcionamiento general del sistema, dificultan su comprensión, mantenimiento y evolución.

El diagnóstico se realizó sobre el código inicial, principalmente sobre la clase `Gestor`, donde se concentra gran parte de la lógica del sistema. Para cada problema identificado se documentó su ubicación, evidencia concreta, impacto sobre el código, riesgo de mantenimiento y prioridad de corrección.

Se identificaron nueve code smells o debilidades técnicas.

---

## Long Method

**Ubicación:** `src/inicial/Gestor.java`, método `procesar()`, líneas 16 a 93.

**Evidencia**

El método `procesar()` realiza varias operaciones diferentes: valida los datos recibidos, lee el archivo de solicitudes, calcula el siguiente identificador, determina la etiqueta y la marca según el tipo y la prioridad, construye el nuevo registro, escribe nuevamente el archivo y además genera un texto que finalmente no es utilizado como resultado.

**Impacto:** El método cuenta con demasiadas responsabilidades y presenta varios niveles de lógica dentro de una misma operación. Para comprender o modificar una regla es necesario analizar prácticamente todo el método.

**Riesgo de mantenimiento:** Cualquier modificación sobre una de las reglas puede afectar accidentalmente otra parte del procesamiento. Esto aumenta el riesgo de introducir errores durante cambios futuros.

**Prioridad de corrección:** Alta.

---

## Long Parameter List

**Ubicación:** `src/inicial/Gestor.java`, método `procesar()`, línea 16.

**Evidencia**

El método recibe individualmente los parámetros `n`, `l`, `t`, `p`, `d` y `ruta`:

```java
public Resultado procesar(String n, String l, String t, String p, String d, String ruta) {
```

Los datos relacionados con una solicitud son enviados como parámetros independientes y, además, todos ellos son de tipo `String`.

**Impacto:** La firma del método es poco experisva y hay un riesgo de intercambiar accidentalmente valores, por ejemplo nombre y legajo o tipo y prioridad.

**Riesgo de mantenimiento:** Si se incorporan nuevos atributos a una solicitud será necesario ampliar la firma del método y modificar las llamadas correspondientes. La cantidad de parámetros también dificulta comprender qué información necesita realmente la operación.

**Prioridad de corrección:** Alta.

---

## Mysterious Name

**Ubicación:** `src/inicial/Gestor.java`, líneas 16 y 17 y diferentes sectores de la clase.

**Evidencia**

Se utilizan nombres de variables y parámetros como:

- `n`
- `l`
- `t`
- `p`
- `d`
- `aux`
- `x`
- `s`
- `e`
- `r`

Estos nombres no permiten identificar directamente qué información representan.

**Impacto:** Para conocer el propósito de una variable es necesario analizar cómo se utiliza dentro del método. Esto reduce la legibilidad y hace más lenta la comprensión del código.

**Riesgo de mantenimiento:** Un desarrollador puede interpretar incorrectamente el significado de una variable y utilizarla de forma equivocada durante una modificación.

**Prioridad de corrección:** Media.

---

## God Class

**Ubicación:** `src/inicial/Gestor.java`, clase `Gestor`.

**Evidencia**

La clase cuenta con demasiadas responsabilidades:

- valida solicitudes
- registra solicitudes
- lee información desde el archivo
- guarda información
- cambia estados
- valida transiciones de estado
- asigna responsables
- realiza búsquedas y filtros
- genera reportes
- transforma los registros almacenados

La misma clase contiene lógica de negocio, persistencia y transformación de datos.

**Impacto:** Se crea un acoplamiento muy alto entre responsabilidades diferentes y una baja cohesión dentro de la clase.

**Riesgo de mantenimiento:** Un cambio relacionado con una responsabilidad obliga a modificar una clase utilizada también por el resto de las operaciones. Esto aumenta el riesgo de afectar funcionalidades que no estaban relacionadas directamente con el cambio.

**Prioridad de corrección:** Alta.

---

## Duplicated Code

**Ubicación:** `src/inicial/Gestor.java`. Lecturas en las líneas 30, 98, 160, 195 y 225. Escrituras en las líneas 69, 147 y 186.

- **Codigo a revisar** - Lectura:

```java
lineas = new ArrayList<>(Files.readAllLines(Path.of(ruta), StandardCharsets.UTF_8));
```

- **Codigo a revisar** - Escritura: 
```java
Files.writeString(Path.of(ruta), contenido.toString(), StandardCharsets.UTF_8);
```
**Evidencia**

La operación de lectura del archivo se encuentra repetida en cinco métodos diferentes. De la misma manera, la lógica utilizada para reconstruir y escribir el contenido del archivo aparece repetida en tres sectores de la clase.

**Impacto:** Si es necesario modificar la forma de leer, escribir o gestionar el archivo este cambio debe repetirse en diferentes sectores del código.

**Riesgo de mantenimiento:** Si una de las copias se modifica y las demás no, pueden aparecer comportamientos diferentes para una misma operación de persistencia. También aumenta la cantidad de código que debe revisarse y mantenerse.

**Prioridad de corrección:** Alta.

---

## Condicional largo

**Ubicación:** `src/inicial/Gestor.java`, líneas 43 a 60 y 112 a 124.

**Evidencia**

El comportamiento del sistema depende de distintas cadenas de condiciones.

Para los tipos de solicitud se evalúan individualmente valores como:

- `Constancia`
- `Equivalencia`
- `Revision`


**Codigo a revisar:**

```java
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
```

La prioridad también se determina mediante condiciones para:

- `Alta`
- `Media`
- `Baja`

**Codigo a revisar**
```java
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
```

Presenta un problema similar, las transiciones entre estados se resuelven mediante diferentes bloques `if` para estados como `Nueva`, `En curso`, `Resuelta` y `Rechazada`.

**Impacto:** Las reglas de clasificación y transición se encuentran incrustadas directamente dentro de métodos que también realizan otras operaciones.

**Riesgo de mantenimiento:** La incorporación de un nuevo tipo, prioridad o estado obliga a modificar los métodos centrales y extender todavía más sus estructuras condicionales. Esto incrementa su complejidad y el riesgo de modificar accidentalmente una condición existente.

**Prioridad de corrección:** Alta.

---

## Excepción escondida

**Ubicación:** `src/inicial/Gestor.java`, bloques `catch` de las líneas 31, 70, 99, 148, 161, 187, 196 y 226.

**Evidencia**

Existen distintos bloques `catch` vacíos que capturan una excepción sin realizar ninguna acción ni informar que ocurrió un problema.

Por ejemplo, ante determinados errores de lectura se continúa trabajando con una colección vacía.

Debe diferenciarse este problema del `catch` ubicado aproximadamente en la línea 90, ya que ese bloque no está vacío y devuelve el mensaje `"Faltan datos de la solicitud"`.


**Codigo a revisar**
```java
catch (Exception ex) {
```
(En las lineas donde no se informe el tipo de error encontrado)



**Impacto:** Un problema con el archivo puede interpretarse como una lista sin solicitudes. Asimismo, determinados errores internos pueden presentarse al usuario como un problema de datos faltantes aunque su causa real sea diferente.

**Riesgo de mantenimiento:** Los fallos reales quedan ocultos o se comunican mediante un mensaje que no representa correctamente su causa. Esto dificulta considerablemente detectar y diagnosticar problemas.

**Prioridad de corrección:** Alta.

---

## Dead Code

**Ubicación:** `src/inicial/Gestor.java`, método `calcular()`, líneas 271 a 279.

**Evidencia**

El método `calcular()` no es utilizado por ninguna de las operaciones actuales del sistema. El propio código incluye el comentario:

`// Quedo de una prueba anterior y nadie lo llama.`

**Codigo a revisar**

```java
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
```

El método realiza además una operación relacionada con los valores `"ALTA"` y `"MEDIA"` que no forma parte de las funcionalidades utilizadas actualmente por Academa.

**Impacto:** La presencia del método puede generar confusión sobre las funcionalidades reales del sistema y hacer pensar que existe algún tipo de cálculo asociado a las solicitudes.

**Riesgo de mantenimiento:** Bajo. El método no interviene actualmente en la ejecución del sistema, por lo que no representa un riesgo funcional inmediato. Sin embargo, aumenta innecesariamente el código que debe comprenderse y mantenerse.

**Prioridad de corrección:** Baja.

---

## Regla en el lugar incorrecto

**Ubicación:** `src/inicial/Gestor.java`, método `armarReporte()`, líneas 234 a 243.

**Codigo a revisar**

```java
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
```

**Evidencia**

El método intenta calcular la etiqueta de una solicitud utilizando `partes[2]`:

`partes[2].equals("Constancia")`

`partes[2].equals("Equivalencia")`

`partes[2].equals("Revision")`

Sin embargo, según el formato utilizado por el archivo:

`id|nombre|legajo|tipo|prioridad|descripcion|estado|responsable|etiqueta|marca`

la posición `partes[2]` corresponde al legajo y no al tipo de solicitud.

El tipo se encuentra en `partes[3]`.

Además, después de realizar este cálculo, el reporte se construye utilizando directamente `partes[8]` y `partes[9]`, por lo que la variable `etiqueta` calculada no modifica el resultado generado.

**Impacto:** Existe lógica redundante dentro de `armarReporte()` y la regla se ejecuta utilizando una columna incorrecta. Aunque actualmente no modifica el resultado final, dificulta comprender cuál es la fuente correcta de la etiqueta.

**Riesgo de mantenimiento:** Un desarrollador podría asumir que esa lógica es utilizada realmente para construir el reporte y modificarla o extenderla, introduciendo nuevos errores. Además, la misma regla queda potencialmente distribuida entre el registro de la solicitud y la generación del reporte.

**Prioridad de corrección:** Media.

---

## Resumen y prioridades

Los nueve hallazgos identificados presentan distintos niveles de impacto y riesgo de mantenimiento.

| Code smell o debilidad | Ubicación | Riesgo de mantenimiento | Prioridad |
|---|---|---|---|
| Long Method | `procesar()`, líneas 16-93 | Alto | Alta |
| Long Parameter List | `procesar()`, línea 16 | Alto | Alta |
| Mysterious Name | `Gestor`, desde líneas 16-17 | Medio | Media |
| God Class | Clase `Gestor` | Alto | Alta |
| Duplicated Code | Lecturas y escrituras de `Gestor` | Alto | Alta |
| Condicional largo | Líneas 43-60 y 112-124 | Alto | Alta |
| Excepción escondida | Bloques `catch` de `Gestor` | Alto | Alta |
| Dead Code | `calcular()`, líneas 271-279 | Bajo | Baja |
| Regla en el lugar incorrecto | `armarReporte()`, líneas 234-243 | Medio | Media |

Los problemas de **prioridad alta** requieren mayor atención debido a que afectan directamente a la complejidad, el acoplamiento y la capacidad de modificar el sistema de forma segura.

`Long Method`, `God Class` y `Duplicated Code` muestran principalmente una concentración y repetición excesiva de responsabilidades. `Long Parameter List` y `Mysterious Name` dificultan comprender con claridad los datos manipulados. El `Condicional largo` concentra reglas que deberán modificarse cada vez que cambien determinados conceptos del dominio.

La `Excepción escondida` presenta además un problema importante para el diagnóstico de errores, ya que algunos fallos pueden ser ignorados o presentados como situaciones diferentes a la causa real.

Por otra parte, `Dead Code` tiene una prioridad baja porque actualmente no interviene en el funcionamiento de la aplicación. La `Regla en el lugar incorrecto` tiene prioridad media porque actualmente el cálculo incorrecto no interviene en el resultado final del reporte, pero representa una fuente de confusión y un riesgo para modificaciones futuras.

