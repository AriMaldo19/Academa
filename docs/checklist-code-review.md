
# Checklist de Code Review — Academa

## 1. Objetivo

Evaluar la calidad técnica del código de Academa, registrar
observaciones y comprobar que la versión refactorizada
conserva el comportamiento esperado del sistema.

Este documento registra los controles realizados, las
evidencias disponibles y los aspectos pendientes de revisión.

## 2. Alcance

Archivos considerados para la revisión:

- `src/inicial/Gestor.java` (referencia inicial, sin modificar)
- `src/refactorizado/` (versión integrada para revisión)
- `src/pruebas/PruebasInicial.java`
- `src/pruebas/Comparacion.java`
- `compilar-y-probar.bat`
- Documentación técnica del proyecto

Estados utilizados:

- [x] Verificado con evidencia.
- [ ] Pendiente de evaluación o resolución.

Una casilla sin marcar no implica necesariamente
un incumplimiento del criterio.

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

### Manejo de errores

- [x] Los errores de lectura y escritura se informan mediante excepciones
  en la implementación refactorizada.
- [x] Se diferencia un archivo inexistente de otros errores de acceso.
- [x] Se verificó el rechazo de solicitudes con datos incompletos.
- [x] Se verificaron transiciones de estado inválidas.
- [x] Se compararon los resultados de validaciones y operaciones inválidas.
- [ ] Se evaluó completamente el tratamiento de archivos mal formados.

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

- [x] Se utiliza una rama propia para pruebas y documentación.
- [x] Los avances anteriores se registraron mediante commits descriptivos.
- [x] Se preparó y aplicó el checklist técnico de code review.
- [x] Se registraron observaciones sobre el código refactorizado.
- [ ] Se realizó la revisión mediante Pull Request.
- [ ] Se publicaron los últimos cambios de pruebas y documentación.
- [ ] Se incorporaron las evidencias definitivas al informe.
- [ ] Se completó la documentación final del proyecto.
- [ ] Se cerraron las observaciones del code review.

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

## 5. Evidencias disponibles

| Evidencia | Archivo o recurso | Estado |
|---|---|---|
| Pruebas iniciales | `src/pruebas/PruebasInicial.java` | Verificada |
| Pruebas comparativas | `src/pruebas/Comparacion.java` | Verificada |
| Script automatizado | `compilar-y-probar.bat` | Verificado |
| Ejecución P01 a P06 | Consola de IntelliJ/Windows | Superada |
| Comparación de 17 operaciones | Consola de IntelliJ/Windows | Superada |
| Comparación de reportes | Salida de `Comparacion.java` | Superada |
| Consulta de archivo inexistente | Salida de `Comparacion.java` | Superada |
| Análisis del refactoring | `src/refactorizado/` | Revisado |
| Capturas para entrega | Informe y documentación | Pendiente de completar |

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

### CR-01 — Tratamiento de archivos mal formados

- **Archivo:** `src/refactorizado/ArchivoSolicitudes.java`
- **Prioridad:** Media.
- **Observación:** las filas con una cantidad insuficiente de
  campos pueden omitirse sin informar explícitamente
  que el archivo presenta datos mal formados.
- **Impacto:** podría ocultarse la existencia de registros
  incompletos o corruptos durante la lectura.
- **Recomendación:** evaluar una validación explícita
  y un mecanismo adecuado para informar el problema.
- **Estado:** observación registrada; resolución pendiente.

### CR-02 — Cantidad de parámetros del constructor

- **Archivo:** `src/refactorizado/Solicitud.java`
- **Prioridad:** Baja.
- **Observación:** el constructor recibe varios parámetros,
  aunque el método `Solicitud.nueva()` simplifica la creación
  habitual de solicitudes.
- **Impacto:** una lista extensa de parámetros puede
  dificultar la lectura y el mantenimiento.
- **Recomendación:** evaluar su simplificación únicamente
  si aporta una mejora concreta al diseño.
- **Estado:** oportunidad de mejora documentada.

### CR-03 — Extensibilidad de tipos y estados

- **Archivos:** `src/refactorizado/TipoSolicitud.java`
  y `src/refactorizado/EstadoSolicitud.java`
- **Prioridad:** Baja.
- **Observación:** incorporar nuevos tipos o estados
  requiere actualizar sus correspondientes métodos `crear()`.
- **Impacto:** la solución mejora la separación de
  responsabilidades, pero conserva puntos que deben
  modificarse al agregar nuevas variantes.
- **Recomendación:** mantener la solución actual mientras
  el alcance no justifique una abstracción adicional.
- **Estado:** limitación de diseño documentada.

## 7. Correcciones realizadas

No se realizaron modificaciones sobre `src/inicial/`.

Se organizaron seis pruebas funcionales independientes
en `PruebasInicial.java`.

Se incorporó `Comparacion.java` para ejecutar y contrastar
17 operaciones sobre ambas versiones.

Se actualizó `compilar-y-probar.bat` para compilar
las implementaciones inicial y refactorizada, y ejecutar
automáticamente ambas clases de prueba.

Durante esta revisión no se realizaron cambios adicionales
en la implementación refactorizada.

Las observaciones CR-01, CR-02 y CR-03 se documentaron
para su evaluación técnica. No se registran todavía
correcciones de implementación derivadas de ellas.

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
- [ ] Se resolvió o aceptó formalmente el tratamiento de CR-01.
- [ ] Se registró la decisión final sobre CR-02 y CR-03.
- [ ] Se completaron las evidencias y la documentación de entrega.
- [ ] Se realizó la aceptación técnica final del equipo.

**Estado general:** pruebas iniciales y comparativas superadas.
Code review realizado con observaciones documentadas.


