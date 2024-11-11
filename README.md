
# Eventify: Share Your Memories 📝🎉

Eventify es una plataforma diseñada para que los usuarios compartan recuerdos y eventos importantes de su vida con sus amigos y seguidores. Con Eventify, puedes crear publicaciones, gestionar comentarios, y mucho más. Desarrollado con **Java** y **Spring Boot**, este proyecto combina la potencia de un backend robusto con la facilidad de uso.

## Integrantes del Proyecto 👥

| Nombre                 | GitHub User       | Email                           |
| ------------------     | ----------------- | -----------------------------   |
| **Giordano Fuentes**   | Gibonn24          | rafael.fuentes@utec.edu.pe     |
| **David Salaverry**    | salaverryyy       | david.salaverry@utec.edu.pe    |
| **Stephany Gutierrez** | 202110740stephany | stephany.gutierrez@utec.edu.pe  |

## Prerrequisitos 🔧

Antes de comenzar, asegúrate de tener lo siguiente instalado en tu máquina:

- **Java**: Recomendado versión 17 o superior
- **PostgreSQL**: Recomendado última versión (16 o superior)
- **Docker**: Última versión
- **IntelliJ IDEA**: Última versión o tu IDE preferido

## Configuración Inicial 🚀

Sigue los siguientes pasos para configurar el proyecto en tu entorno local:

### 1. Clona el Repositorio

Abre tu terminal y ejecuta el siguiente comando:

```bash
git clone https://github.com/salaverryyy/Eventify
```

### 2. Navega al Directorio del Proyecto

```bash
cd Eventify
```

### 3. Abre el Proyecto en tu IDE Preferido

Te recomendamos usar **IntelliJ IDEA**. Abre el proyecto seleccionando el directorio clonado.

### 4. Configura la Base de Datos

Asegúrate de tener **Docker** instalado. Luego, ejecuta el siguiente comando para levantar la base de datos PostgreSQL:

```bash
docker-compose up
```

#### Configuración de Docker:

```yaml
services:
  db:
    image: postgres:latest
    container_name: eventify-db
    restart: always
    environment:
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: postgres
    ports:
      - "5432:5432"
```

### 5. Ejecuta la Aplicación

Ejecuta la clase principal `EventifyApplication` desde tu IDE. **Maven** se encargará automáticamente de gestionar las dependencias a través del archivo `pom.xml`.

### 6. Poblar la Base de Datos

Después de ejecutar la aplicación, ejecuta los scripts que se encuentran en la carpeta `scripts` para poblar la base de datos con datos iniciales.

## Diagrama Entidad-Relación (ERD)

[ER Diagram](#)

## Endpoints 📡

A continuación, se muestra una lista de los principales endpoints disponibles en el backend:

### **Publicaciones 📝**

| Método | Endpoint                           | Descripción                                  |
| ------ | ----------------------------------- | -------------------------------------------- |
| GET    | /post/{id}                          | Obtener una publicación por su ID            |
| GET    | /post/user/{userId}                 | Obtener publicaciones por ID de usuario      |
| POST   | /post                              | Crear una nueva publicación                  |
| PATCH  | /post/{id}                          | Actualizar una publicación por su ID         |
| DELETE | /post/{id}                          | Eliminar una publicación por su ID           |

### **Comentarios 💬**

| Método | Endpoint                           | Descripción                                  |
| ------ | ----------------------------------- | -------------------------------------------- |
| GET    | /comments/post/{postId}             | Obtener comentarios de una publicación       |
| POST   | /comments                           | Crear un nuevo comentario                    |
| DELETE | /comments/{commentId}               | Eliminar un comentario por su ID             |

### **Usuarios 👤**

| Método | Endpoint                           | Descripción                                  |
| ------ | ----------------------------------- | -------------------------------------------- |
| GET    | /user/{id}                          | Obtener información de usuario por ID        |
| GET    | /user/me                            | Obtener información del usuario actual       |
| PATCH  | /user/update/me                     | Actualizar la información del usuario actual |

### **Autenticación 🔐**

| Método | Endpoint                           | Descripción                                  |
| ------ | ----------------------------------- | -------------------------------------------- |
| POST   | /auth/login                         | Iniciar sesión                               |
| POST   | /auth/signup                        | Registrar un nuevo usuario                   |

## Cómo Contribuir 🤝

1. Haz un fork del proyecto.
2. Crea una nueva rama (`git checkout -b feature-nueva-funcionalidad`).
3. Realiza los cambios necesarios y haz commit (`git commit -am 'Agrego nueva funcionalidad'`).
4. Sube los cambios a tu repositorio (`git push origin feature-nueva-funcionalidad`).
5. Crea un nuevo **Pull Request** para que revisemos tu contribución.

## Agradecimientos 🫶

Queremos agradecer a todos los que apoyaron este proyecto con sus pruebas y retroalimentación valiosa. Un agradecimiento especial a los TA del curso y al profesor que nos acompañó durante esta aventura.

## Licencia 📄

Este proyecto está bajo la Licencia **MIT**. Puedes ver más detalles en el archivo [LICENSE](LICENSE).

---


