
# Sistema de Gestión de Guardería Central

## Descripción del Proyecto
Este proyecto es una solución de software desarrollada en **Java** para la gestión integral de una guardería de vehículos. El sistema permite administrar socios, vehículos, garajes, zonas y sus correspondientes asignaciones, utilizando un diseño orientado a objetos con arquitectura en capas.

## Características Principales
* **Gestión de Socios:** Registro y control de miembros.
* **Administración de Vehículos:** Gestión del parque automotor vinculado a socios.
* **Gestión de Garages y Zonas:** Organización de espacios físicos y mantenimiento.
* **Validaciones de Negocio:** Implementación de reglas estrictas (fechas, capacidades, exclusividad) a través de una capa de servicios.
* **Persistencia en Archivos:** Los datos se almacenan de forma persistente utilizando archivos planos (CSV), garantizando la integridad de la información.

## Credenciales de Acceso para Pruebas
Para facilitar la evaluación y verificación del sistema, se han configurado los siguientes usuarios de prueba mediante la clase `InicializarDataBase.java`. Puedes utilizar estas credenciales para acceder a las distintas funcionalidades según el rol:

| Rol                    | Usuario      | Contraseña    |
|:-----------------------|:-------------|:--------------|
| **SuperAdministrador** | `superadmin` | `super123`    |
| **Administrador**      | `admin`      | `admin123`    |
| **Empleado**           | `empleado`   | `empleado123` |
| **Socio**              | `socio`      | `socio123`    |

*Nota: Estas cuentas se crean automáticamente en la primera ejecución si la base de datos está vacía.*

## Arquitectura del Sistema
El sistema sigue un diseño de **arquitectura en capas** para asegurar la mantenibilidad y el orden:
- **Model:** Entidades base (POJOs) con validaciones en el constructor.
- **DAO (Data Access Object):** Capa encargada de la persistencia y lectura de archivos.
- **Service:** Capa de lógica de negocio que actúa como filtro (aquí se aplican las reglas de validación).
- **Controller/View:** Interfaz de usuario para la interacción y visualización de resultados.
- **Exceptions:** Manejo personalizado de errores para una experiencia de usuario robusta.

## Requisitos de Ejecución
- **Java Development Kit (JDK):** versión 21.
- **Entorno de desarrollo:** NetBeans

## Instrucciones de Ejecución
1. Clonar o descargar el repositorio.
2. Compilar el proyecto utilizando el IDE de preferencia.
3. Ejecutar la clase principal: Main.java.
4. El sistema creará automáticamente los archivos de datos si no existen.



## Documentación Técnica (Javadoc)
Para consultar la documentación técnica detallada de las clases y métodos, por favor abra el archivo `index.html` ubicado en la carpeta `docs/javadoc` del proyecto.

---
**Desarrollado por:** Franco Buyatti, Daniela Forclaz, Héctor Machaca y María Eugenia Barca 
**Materia:** Programación II - INSPT UTN - Tecnicatura en Informatica Aplicada - Turno Noche - Comisión: 2.603  