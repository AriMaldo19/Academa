

# Checklist de Code Review — Academa

## 1. Objetivo

Evaluar la calidad técnica del código de Academa, registrar
observaciones y comprobar que la versión refactorizada
conserva el comportamiento esperado del sistema.

Este documento registra los controles realizados, las evidencias
obtenidas y las decisiones técnicas adoptadas durante la revisión.

El code review se considera finalizado con tres observaciones
documentadas y aceptadas, sin requerir modificaciones adicionales
en la implementación.

## 2. Alcance

Archivos considerados para la revisión:

- `src/inicial/Gestor.java` (referencia inicial, sin modificar)
- `src/refactorizado/` (versión refactorizada integrada)
- `src/pruebas/PruebasInicial.java`
- `src/pruebas/Comparacion.java`
- `compilar-y-probar.bat`
- Documentación técnica del proyecto

Estados utilizados:

- [x] Criterio verificado con evidencia.
- [x] Observación evaluada y aceptada mediante una decisión justificada.

La aceptación de observaciones no implica que el código esté
libre de limitaciones, sino que se evaluó su impacto y se
consideró adecuado conservar la implementación actual.

## 3. Checklist técnico

### Clean Code

- [x] Los nombres de clases y métodos refactorizados son descriptivos.
- [x] Los métodos principales tienen responsabilidades claras.
- [x] Se redujo la duplicación de código relacionada con el acceso a archivos.
- [x] El código refactorizado resulta legible y comprensible.

### SOLID y diseño

- [x] Las responsabilidades se distribuyen entre clases específicas.
- [x] Se observa una mejora en la cohesión de los componentes.
- [x] Se redujo el acoplamiento entre la lógica de negocio y la persistencia.
- [x] Las decisiones principales de diseño están justificadas.
- [x] Se identificaron las aplicaciones de SRP y OCP.
- [x] Se revisaron los patrones Strategy y State.
- [x] Se identificaron las técnicas Extract Method, Move Method,
  Introduce Parameter Object y Replace Conditional.
- [x] Se evaluaron las limitaciones de extensibilidad de los
  métodos de creación de tipos y estados.

### Manejo de errores

- [x] Los errores de lectura y escritura se informan mediante excepciones
  en la implementación refactorizada.
- [x] Se diferencia un archivo inexistente de otros errores de acceso.
- [x] Se verificó el rechazo de solicitudes con datos incompletos.
- [x] Se verificaron transiciones de estado inválidas.
- [x] Se compararon los resultados de validaciones y operaciones inválidas.
- [x] Se identificó y evaluó el tratamiento de archivos mal formados.
- [x] Se documentó y aceptó la limitación correspondiente a CR-01.

### Pruebas y verificación

- [x] Se implementaron seis pruebas funcionales sobre la versión inicial.
- [x] Cada prueba inicial utiliza un archivo temporal independiente.
- [x] Las pruebas iniciales eliminan sus archivos mediante bloques `finally`.
- [x] Las pruebas comprueban resultados esperados mediante aserciones.
- [x] Se verificaron registro, validación, cambios de estado,
  asignación de responsables y generación de reportes.
- [x] Las seis pruebas iniciales se ejecutaron correctamente.
- [x] Se implementó `src/pruebas/Comparacion.java`.
- [x] Se ejecutaron 17 operaciones comparativas sobre ambas versiones.
- [x] Se verificó la igualdad de los reportes generados.
- [x] Se verificó el comportamiento ante archivos inexistentes.
- [x] Se compararon resultados, mensajes y datos observables.
- [x] No se detectaron diferencias entre versiones
  en los escenarios evaluados.
- [x] El script `compilar-y-probar.bat` compila ambas versiones
  y ejecuta las pruebas iniciales y comparativas.
- [x] La ejecución completa finalizó correctamente.

### Git y documentación

- [x] Se utilizó una rama propia para pruebas y documentación.
- [x] Los avances se registraron mediante commits descriptivos.
- [x] Las pruebas y el checklist se integraron en `main`.
- [x] Se preparó y aplicó el checklist técnico de code review.
- [x] Se registraron tres observaciones sobre el código refactorizado.
- [x] Se evaluó la prioridad y el impacto de cada observación.
- [x] Se documentaron las decisiones de aceptación.
- [x] Se justificó la conservación de la implementación actual.
- [x] Se estableció el resultado de aceptación técnica del code review.

## 4. Pruebas funcionales implementadas

### 4.1 Pruebas sobre la versión inicial

| ID | Caso de prueba | Resultado |
|---|---|---|
| P01 | Registro de solicitud válida | Superada |
| P02 | Rechazo de solicitud incompleta | Superada |
| P03 | Cambio de estado Nueva a En curso | Superada |
| P04 | Rechazo de transición Nueva a Resuelta | Superada |
| P05 | Asignación de responsable | Superada |
| P06 | Generación de reporte | Superada |

Las seis pruebas se ejecutaron sobre la versión inicial.

Cada caso utiliza datos de prueba y archivos temporales
independientes para evitar modificaciones en los archivos
originales de la aplicación.

### 4.2 Pruebas comparativas

Se implementó `src/pruebas/Comparacion.java` para comprobar
el comportamiento de las versiones inicial y refactorizada.

La comparación comprende:

- 6 operaciones de registro.
- 6 operaciones de cambio de estado.
- 3 operaciones de asignación de responsables.
- 2 operaciones de búsqueda.
- Comparación de reportes generados.
- Verificación de consultas a archivos inexistentes.

**Resultado:** las dos versiones coincidieron en las 17
operaciones evaluadas, en el reporte y ante archivos inexistentes.

La equivalencia se limita a los escenarios efectivamente
evaluados y no representa una cobertura completa del sistema.

## 5. Evidencias de verificación

| Evidencia | Archivo o recurso | Resultado |
|---|---|---|
| Pruebas iniciales | `src/pruebas/PruebasInicial.java` | Verificada |
| Pruebas comparativas | `src/pruebas/Comparacion.java` | Verificada |
| Script automatizado | `compilar-y-probar.bat` | Verificado |
| Ejecución P01 a P06 | Consola de IntelliJ/Windows | Superada |
| Comparación de 17 operaciones | Consola de IntelliJ/Windows | Superada |
| Comparación de reportes | Salida de `Comparacion.java` | Superada |
| Consulta de archivo inexistente | Salida de `Comparacion.java` | Superada |
| Análisis del refactoring | `src/refactorizado/` | Revisado |
| Observaciones técnicas | Sección 6 de este documento | Documentadas |
| Captura de pruebas iniciales | `docs/capturas/pruebas-iniciales.png` | Disponible |
| Captura de pruebas comparativas | `docs/capturas/pruebas-comparativas.png` | Disponible |

Las ejecuciones automatizadas proporcionan evidencia
de conservación del comportamiento en los escenarios
evaluados, sin constituir una garantía de ausencia
de defectos en situaciones no contempladas.

## 6. Observaciones de la revisión

La versión inicial de Academa se conservó sin modificaciones
para permitir la comparación antes y después del refactoring.

La versión refactorizada presenta una distribución de
responsabilidades más clara mediante clases como
`Solicitud`, `DatosSolicitud`, `ArchivoSolicitudes`,
`TipoSolicitud` y `EstadoSolicitud`.

Se verificó la aplicación de las técnicas de refactoring
documentadas y se ejecutaron pruebas comparativas
sin detectar diferencias en los escenarios evaluados.

Durante el code review se identificaron tres observaciones.
Se evaluaron su impacto, prioridad y relación con el alcance
del proyecto, y se decidió aceptarlas sin modificar el código.

### CR-01 — Tratamiento de archivos mal formados

- **Archivo:** `src/refactorizado/ArchivoSolicitudes.java`
- **Prioridad:** Media.
- **Observación:** las filas con una cantidad insuficiente de
  campos pueden omitirse sin informar explícitamente
  que el archivo presenta datos mal formados.
- **Impacto:** podría ocultarse la existencia de registros
  incompletos o corruptos durante la lectura.
- **Decisión:** aceptar la limitación conocida sin modificar
  el comportamiento de la implementación actual.
- **Justificación:** las verificaciones funcionales realizadas
  utilizan archivos válidos y no detectaron regresiones.
  El tratamiento de archivos corruptos queda reconocido
  como una posibilidad de mejora de robustez, fuera del
  alcance de las refactorizaciones implementadas.
- **Estado:** observación aceptada y documentada.

### CR-02 — Cantidad de parámetros del constructor

- **Archivo:** `src/refactorizado/Solicitud.java`
- **Prioridad:** Baja.
- **Observación:** el constructor recibe nueve parámetros,
  aunque el método `Solicitud.nueva()` simplifica la creación
  habitual de solicitudes.
- **Impacto:** una lista extensa de parámetros puede
  dificultar la lectura y aumentar el riesgo de errores
  al proporcionar argumentos.
- **Decisión:** conservar el constructor actual.
- **Justificación:** la implementación ya dispone de
  mecanismos que simplifican la creación de solicitudes.
  Una nueva reorganización del constructor no aportaría
  una mejora significativa para el alcance del sistema.
- **Estado:** observación aceptada y documentada.

### CR-03 — Extensibilidad de tipos y estados

- **Archivos:** `src/refactorizado/TipoSolicitud.java`
  y `src/refactorizado/EstadoSolicitud.java`
- **Prioridad:** Baja.
- **Observación:** incorporar nuevos tipos o estados
  requiere actualizar sus correspondientes métodos `crear()`.
- **Impacto:** la aplicación de OCP es parcial, ya que
  agregar nuevas variantes requiere modificar los métodos
  encargados de seleccionar sus implementaciones.
- **Decisión:** conservar el mecanismo de creación actual.
- **Justificación:** Academa trabaja con un conjunto acotado
  de tipos y estados. La solución mejora la separación
  de responsabilidades y evita incorporar mecanismos
  de extensibilidad más complejos sin una necesidad concreta.
- **Estado:** observación aceptada y documentada.

## 7. Correcciones y decisiones adoptadas

Se organizaron seis pruebas funcionales independientes
en `PruebasInicial.java`.

Se incorporó `Comparacion.java` para ejecutar y contrastar
17 operaciones sobre ambas versiones.

Se actualizó `compilar-y-probar.bat` para compilar
las implementaciones inicial y refactorizada, y ejecutar
automáticamente ambas clases de prueba.

Como resultado del code review se registraron
observaciones técnicas, evaluadas según su gravedad,
impacto y alcance.

Se decidió aceptar las observaciones
sin realizar nuevas modificaciones sobre la implementación.

Esta decisión constituye el cierre de las observaciones,
no la corrección de sus limitaciones.

## 8. Criterios de aceptación

- [x] La versión refactorizada compila correctamente.
- [x] Se ejecutan pruebas funcionales sobre la versión inicial.
- [x] Se ejecutan operaciones comparativas sobre ambas versiones.
- [x] Se conservan los resultados esperados en los casos evaluados.
- [x] No se detectaron regresiones en los escenarios comparados.
- [x] Los refactorings principales están identificados y justificados.
- [x] Las responsabilidades están distribuidas entre clases específicas.
- [x] Se comprobó una mejora en el manejo de excepciones de E/S.
- [x] Las observaciones del code review están documentadas.
- [x] Se evaluó y aceptó la limitación CR-01.
- [x] Se justificó la conservación del constructor señalado en CR-02.
- [x] Se justificó la decisión de diseño correspondiente a CR-03.
- [x] Se documentaron las decisiones adoptadas.
- [x] Se completó la revisión técnica de la versión refactorizada.
- [x] Se estableció la aceptación técnica con observaciones conocidas.

## 9. Conclusión del code review

La revisión técnica permitió comprobar que la versión
refactorizada de Academa presenta mejoras en la legibilidad,
distribución de responsabilidades, organización de las reglas
de negocio y manejo de errores de entrada/salida.

Las pruebas iniciales y comparativas finalizaron correctamente,
sin detectar diferencias de comportamiento entre ambas
implementaciones en los escenarios evaluados.

El code review identificó tres observaciones relacionadas
con la robustez de la lectura de archivos, la cantidad de
parámetros de un constructor y la extensibilidad de los
tipos y estados.

Estas observaciones fueron evaluadas y aceptadas como
limitaciones conocidas o decisiones de diseño justificadas,

**Resultado final: CODE REVIEW APROBADO CON OBSERVACIONES
DOCUMENTADAS Y ACEPTADAS.**


