# 👕 Outlet de Ropa — Gestión de inventario en Java Swing (MVC)

Aplicación de escritorio en **Java** para gestionar el inventario de un outlet de ropa. Desarrollada como práctica del módulo **Acceso a Datos** de 2º de **DAM** (Desarrollo de Aplicaciones Multiplataforma).

## ✨ Funcionalidades
- Gestión de tres tipos de prenda: **camisetas, pantalones y zapatillas**
- **Alta, modificación y eliminación** de prendas
- Datos por prenda: código, marca, nombre, precio, descuento, color, fecha de entrada, talla y descripción
- Listado con **búsqueda**
- Validación de **código único** por prenda
- Selección de fecha de entrada con calendario (LGoodDatePicker)
- **Exportación e importación de datos en XML** (DOM)
- Fichero de configuración `outlet.conf`

## 🏗️ Arquitectura
El proyecto sigue el patrón **Modelo–Vista–Controlador (MVC)**:

| Capa | Clases |
|------|--------|
| Modelo de datos | `Prenda` (clase base) → `Camiseta`, `Pantalon`, `Zapatilla` |
| Modelo / lógica | `OutletModelo` — gestión de la lista de prendas e import/export XML |
| Vista | `Ventana` — interfaz gráfica en Java Swing |
| Controlador | `OutletControlador` — conecta los eventos de la vista con el modelo |

Punto de entrada: `com.carlosdinu.outlet.Principal`

## 📸 Capturas
![Aplicación con prendas cargadas](docs/Captura%20de%20pantalla%202026-11-23%20193055.png)
![Formulario vacío](docs/Captura%20de%20pantalla%202026-11-23%20193036.png)

## 🛠️ Tecnologías
- Java · Java Swing
- XML (DOM: `DocumentBuilder`, `Transformer`)
- Herencia y polimorfismo (POO)
- Librerías: LGoodDatePicker, JGoodies Forms
- IntelliJ IDEA · Git
- Documentación generada con **Javadoc** (carpeta `javadoc`)

## 🚀 Cómo ejecutarlo
1. Clona el repositorio:
   ```
   git clone https://github.com/carlosdinu/outlet-ropa_mvc.git
   ```
2. Ábrelo con IntelliJ IDEA.
3. Añade los `.jar` de la carpeta `libs` como librerías del proyecto si no se cargan solos.
4. Ejecuta la clase `com.carlosdinu.outlet.Principal`.

## 👤 Autor
**Carlos Alejandro Dinu Sima** · Estudiante de DAM
[LinkedIn](https://www.linkedin.com/in/carlos-dinu-a458b2451) · [disidev.com](https://disidev.com)
