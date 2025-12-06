# 🚀 MultiserviciosWeb - Backend API

Este proyecto es el Backend de la plataforma **MultiserviciosWeb**, desarrollado con **Spring Boot** y **Java 21**.

La arquitectura está completamente **dockerizada**, lo que permite desplegar la API y la Base de Datos (PostgreSQL) con un solo comando, incluyendo la carga automática de datos de prueba.

## 🛠️ Tecnologías Utilizadas

- **Java 21 (LTS):** Última versión de soporte a largo plazo.
- **Spring Boot 3.x:** Framework para la creación de microservicios.
- **PostgreSQL 16+:** Base de datos relacional.
- **Docker & Docker Compose:** Orquestación de contenedores.
- **Maven:** Gestión de dependencias.

---

## 🏗️ Arquitectura de Despliegue (Docker)

El proyecto utiliza una arquitectura de contenedores para asegurar que funcione idéntico en cualquier máquina.

### 1\. El Dockerfile (Optimizado)

Para la imagen del Backend, utilizamos `bellsoft/liberica-openjdk-alpine:21`.
**¿Por qué?**

- **Alpine Linux:** Es una distribución extremadamente ligera (aprox 5MB base), lo que hace que la imagen final pese mucho menos y se descargue más rápido.
- **Seguridad:** Al tener menos paquetes instalados, la superficie de ataque es menor.

<!-- end list -->

```dockerfile
# Dockerfile utilizado
FROM bellsoft/liberica-openjdk-alpine:21
ARG JAR_FILE=target/web-0.0.1.jar
COPY ${JAR_FILE} app_web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app_web.jar"]
```

### 2\. Base de Datos Autogestionada

La base de datos PostgreSQL se levanta automáticamente.

- **Persistencia:** Los datos se guardan en un volumen de Docker (`db_data`) para no perderse al reiniciar.
- **Auto-Seed (Carga Inicial):** El sistema detecta automáticamente el archivo `SolMaster.sql` y carga toda la estructura y datos iniciales la primera vez que se inicia el contenedor.

---

## ⚙️ Pre-requisitos

1.  Tener instalado **Docker Desktop** o **Docker Engine**.
2.  Tener instalado **Java 21** y **Maven** (solo para compilar el `.jar`).

---

## 🚀 Instalación y Ejecución

Sigue estos pasos para levantar el entorno completo:

### 1\. Configurar Variables de Entorno

Crea un archivo llamado `.env` en la raíz del proyecto (basado en el template) con el siguiente contenido:

```properties
DB_URL=jdbc:postgresql://databasepostgres:5432/SolMaster
DB_USER=postgres
DB_PASSWORD=110500
DB_NAME=SolMaster
# Agrega aquí tus credenciales de MercadoPago si es necesario
```

### 2\. Generar el Ejecutable (JAR)

Compila el proyecto y salta los tests para agilizar el proceso:

```bash
mvn clean package -DskipTests
```

### 3\. Construir y Levantar Contenedores

Ejecuta Docker Compose. Esto construirá la imagen de Java y descargará PostgreSQL:

```bash
docker-compose up --build
```

✅ **Resultado:** La API estará disponible en `http://localhost:8080`.

---

## 🧹 Comandos Útiles

### Detener el servidor (Manteniendo los datos)

```bash
docker-compose stop
```

### Reinicio de Fábrica (Borrar todo y recargar Backup)

Si necesitas reiniciar la base de datos desde cero (borrando los datos nuevos y volviendo a cargar el backup `SolMaster.sql`), usa:

```bash
docker-compose down -v
docker-compose up --build
```

> **Nota:** La bandera `-v` elimina los volúmenes persistentes.

---

## 🐛 Solución de Problemas Comunes

**Error: `Bind for 0.0.0.0:5432 failed: port is already allocated`**

- **Causa:** Tienes un PostgreSQL local ejecutándose en tu PC que ocupa el puerto.
- **Solución:** Detén tu servicio local de Postgres.
  - Linux: `sudo systemctl stop postgresql`
  - Windows: Detener el servicio "PostgreSQL" desde `services.msc`.

**Error: La base de datos está vacía**

- Asegúrate de haber ejecutado `docker-compose down -v` si cambiaste el archivo SQL, ya que el script de inicialización solo corre en volúmenes nuevos.

---
