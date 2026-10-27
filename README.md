# Academa

Academa es el sistema de gestión de solicitudes académicas desarrollado para este trabajo práctico.

El sistema simula una mesa de entrada académica: permite registrar solicitudes, clasificarlas por tipo, prioridad y estado, asignar responsables, realizar búsquedas y consultar un reporte simple.

Está desarrollado en Java. La interfaz es una página HTML con CSS que se abre en el navegador. Las dos versiones presentan la misma interfaz y funcionalidad; las diferencias se encuentran en la estructura interna del código.

Los datos son ficticios y se guardan en archivos de texto, con una solicitud por línea.

## Versiones

- `src/inicial/`: versión de partida, con los problemas de diseño identificados para el diagnóstico.
- `src/refactorizado/`: versión mejorada, con responsabilidades distribuidas entre clases como `Solicitud`, `Estudiante`, `Responsable`, `Estado` y los tipos de solicitud.

## Requisitos

- Java 21 o una versión compatible que incluya `javac`.
- Un navegador web.
- Windows para ejecutar los archivos `.bat`.

## Cómo abrirlo

Desde la carpeta principal del proyecto, hacer doble clic en el archivo correspondiente:

- `ejecutar-inicial.bat`: ejecuta la versión inicial.
- `ejecutar-refactorizada.bat`: ejecuta la versión refactorizada.
- `compilar-y-probar.bat`: compila y ejecuta las pruebas comparativas.

Cada versión abre la interfaz en el navegador y mantiene abierta la ventana de consola mientras se ejecuta el programa. La ventana se cierra cuando se termina de usar esa versión.

Los archivos `solicitudes-inicial.txt` y `solicitudes-refactorizada.txt` almacenan los datos de cada versión cuando se generan durante la ejecución.

## Operaciones

- **Registrar:** ingresar nombre, legajo, tipo, prioridad y descripción.
- **Cambiar estado:** una solicitud nueva pasa a En curso. Una solicitud en curso puede pasar a Resuelta o Rechazada. Las transiciones no permitidas se rechazan.
- **Asignar responsable:** asociar un responsable a una solicitud.
- **Buscar:** filtrar por tipo, prioridad o estado. La opción «(cualquiera)» no aplica ese filtro.
- **Ver reporte:** consultar un resumen de las solicitudes registradas.

Tipos de solicitud: Constancia, Equivalencia y Revisión.

Prioridades: Alta, Media y Baja.

La descripción no debe contener el carácter `|`, porque se utiliza como separador en el archivo de datos.

## Estructura del proyecto

- `src/inicial/`: código de la versión original.
- `src/refactorizado/`: código de la versión refactorizada.
- `src/interfaz/`: componentes compartidos de la interfaz.
- `src/pruebas/`: pruebas comparativas.
- `web/`: archivos de la interfaz web.
- `docs/`: diagnóstico, refactorizaciones, checklist de revisión y evidencias.

## Tecnologías

- Java
- HTML
- CSS
- Git y GitHub para el control de versiones y el trabajo colaborativo.
- Windows Batch (`.bat`), para automatizar la ejecución y las pruebas del proyecto.

---

## 👥 Estudiantes  

- **Maldonado Ariana, Comisión 10**  
- **Carro Matias Manuel, Comisión 11**  
- **Bohorquez Tello Tomas Esteban, Comisión 6**  
- **Andrelo Luciano Aldo, Comisión 7**   
