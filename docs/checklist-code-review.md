
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
- `src/refactorizado/` (pendiente de integración)
- `src/pruebas/PruebasInicial.java`
- `compilar-y-probar.bat`
- Documentación técnica del proyecto

Estados utilizados:

- `[x]` Verificado con evidencia.
- `[ ]` Pendiente de evaluación.

Una casilla sin marcar no implica necesariamente
un incumplimiento del criterio.

## 3. Checklist técnico

### Clean Code

- [ ] Los nombres de clases y métodos son descriptivos.
- [ ] Los métodos tienen responsabilidades claras.
- [ ] No existe duplicación innecesaria.
- [ ] El código resulta legible y comprensible.

### SOLID y diseño

- [ ] Las clases tienen responsabilidades bien definidas.
- [ ] Existe cohesión adecuada.
- [ ] Se evita el acoplamiento innecesario.
- [ ] Las decisiones de diseño están justificadas.

### Manejo de errores

- [ ] Los errores se informan adecuadamente.
- [ ] No se ocultan excepciones relevantes.
- [x] Las pruebas iniciales verifican el rechazo de datos
  incompletos y transiciones de estado inválidas.
- [ ] Se verificó el manejo de errores de la versión refactorizada.

### Pruebas y verificación

- [x] Se implementaron seis pruebas funcionales
  sobre la versión inicial.
- [x] Cada prueba utiliza un archivo temporal independiente.
- [x] Las pruebas eliminan los archivos temporales mediante
  bloques `finally`.
- [x] Las pruebas comprueban resultados esperados mediante
  aserciones.
- [x] Se comprobaron operaciones de registro, validación,
  cambios de estado, asignación y generación de reportes.
- [x] Las seis pruebas se ejecutaron correctamente.
- [x] Las pruebas pueden ejecutarse mediante
  `compilar-y-probar.bat`.
- [ ] Se ejecutaron las mismas pruebas sobre la versión
  refactorizada.
- [ ] Se compararon los resultados de ambas versiones.
- [ ] Se verificó que la refactorización no introdujo regresiones.

### Git y documentación

- [x] Se utiliza una rama propia para el desarrollo
  de las pruebas y la documentación.
- [x] Los avances se registraron mediante commits descriptivos.
- [x] Se preparó el checklist técnico de code review.
- [ ] Se realizó la revisión mediante Pull Request.
- [ ] Se registraron observaciones y correcciones
  del código refactorizado.
- [ ] Se actualizaron las evidencias y el informe final.
- [ ] Se verificaron los criterios de aceptación técnica.

## 4. Pruebas funcionales implementadas

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

Estos resultados no demuestran todavía la equivalencia
entre la versión inicial y la refactorizada.

## 5. Evidencias disponibles

| Evidencia | Archivo o recurso | Estado |
|---|---|---|
| Código de pruebas | `src/pruebas/PruebasInicial.java` | Disponible |
| Script de ejecución | `compilar-y-probar.bat` | Disponible |
| Ejecución de P01 a P06 | Consola de IntelliJ/Windows | Verificada |
| Pruebas comparativas | Pendiente | No ejecutadas |
| Capturas definitivas | `docs/capturas/` | Pendiente |
| Revisión de refactorización | `src/refactorizado/` | Pendiente |

## 6. Observaciones de la revisión

La versión inicial de Academa se conserva sin modificaciones
para permitir la comparación antes/después requerida por el TPI.

Se preparó una batería de seis pruebas funcionales que permiten
verificar comportamientos representativos del sistema.

La revisión formal del código refactorizado permanece pendiente
hasta que dicha implementación esté integrada en el repositorio.

### Observaciones pendientes

- Evaluar la distribución de responsabilidades.
- Revisar los refactorings implementados.
- Comprobar el cumplimiento de los criterios de Clean Code y SOLID.
- Revisar el manejo de errores.
- Ejecutar las pruebas comparativas.
- Registrar hallazgos y recomendaciones técnicas.

## 7. Correcciones realizadas

No se realizaron modificaciones sobre `src/inicial/`.

Se reorganizó `PruebasInicial.java` para separar los casos
funcionales en métodos independientes, cada uno con su
propio archivo temporal.

No se registran todavía correcciones derivadas del code review
de la versión refactorizada.

## 8. Criterios de aceptación

Para considerar completada la revisión técnica deberán
verificarse los siguientes criterios:

- [ ] La versión refactorizada compila correctamente.
- [ ] Las pruebas funcionales se ejecutan sobre ambas versiones.
- [ ] Los resultados esperados se conservan.
- [ ] No se detectan regresiones en los escenarios evaluados.
- [ ] Los refactorings implementados están justificados.
- [ ] Las responsabilidades están distribuidas adecuadamente.
- [ ] Los errores relevantes se manejan correctamente.
- [ ] Las observaciones del code review están documentadas.
- [ ] Las correcciones necesarias fueron verificadas.
- [ ] Las evidencias correspondientes están disponibles.

**Estado general:** preparación y pruebas iniciales completadas.
Code review de la refactorización pendiente.

