# Tienda Gamer - Sistema de Microservicios

Sistema completo de e-commerce para tienda gamer implementado con arquitectura de microservicios usando Spring Boot.

---

## 📋 Tabla de Contenidos

- [Arquitectura de Microservicios](#arquitectura-de-microservicios)
- [Puertos de Servicios](#puertos-de-servicios)
- [Bases de Datos](#bases-de-datos)
- [Flujo del Proyecto](#flujo-del-proyecto)
- [Comunicación entre Servicios](#comunicación-entre-servicios)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
- [API Gateway - Rutas](#api-gateway---rutas)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Notas Importantes](#notas-importantes)

---

## 🏗️ Arquitectura de Microservicios

```
                        ┌─────────────────┐
                        │   API GATEWAY   │
                        │   TIENDA:8085   │
                        └────────┬────────┘
                                 │
                ┌────────────────┼────────────────┐
                │                │                │
        ┌───────▼──────┐  ┌─────▼──────┐  ┌─────▼──────┐
        │  INGRESO:8080│  │REGISTRO:8084│  │VENTAS:8087 │
        └──────┬───────┘  └──────┬──────┘  └─────┬──────┘
               │                 │                │
        ┌──────▼──────┐   ┌──────▼──────┐  ┌─────▼──────┐
        │USUARIOS:8086│   │USUARIOS:8086│  │BOLETA:8088 │
        └─────────────┘   └─────────────┘  └────────────┘
               │                                  │
        ┌──────▼──────┐                    ┌─────▼──────┐
        │PRODUCTOS:8083│◄───────────────────┤VENTAS:8087 │
        └─────────────┘                    └────────────┘
```

---

## 🔌 Puertos de Servicios

| Servicio | Puerto | Descripción | Base de Datos |
|----------|--------|-------------|---------------|
| **INGRESO** | 8080 | Autenticación y login de usuarios | `tiendagamer` |
| **PRODUCTOS** | 8083 | Gestión del catálogo de productos | `tiendagamer` |
| **REGISTRO** | 8084 | Registro de nuevos usuarios | `tiendagamer` |
| **TIENDA** (Gateway) | 8085 | Punto de entrada único - API Gateway | N/A |
| **USUARIOS** | 8086 | Gestión de credenciales (email/password) | `tiendagamer` |
| **VENTAS** | 8087 | Procesamiento y registro de ventas | `tiendagamer` |
| **BOLETA** | 8088 | Generación automática de boletas | `tiendagamer` |

---

## 🗄️ Bases de Datos

### Base de Datos Compartida: `tiendagamer`

**Tablas por Servicio:**

#### REGISTRO
```sql
CREATE TABLE registro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL
);
```

#### USUARIOS
```sql
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
```

#### PRODUCTOS
```sql
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL,
    descripcion VARCHAR(500),
    stock INT NOT NULL,
    categoria VARCHAR(255) NOT NULL
);
```

#### VENTAS
```sql
CREATE TABLE ventas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    total DOUBLE NOT NULL,
    fecha DATETIME NOT NULL,
    boleta_id BIGINT,
    estado VARCHAR(255) NOT NULL DEFAULT 'COMPLETADA'
);
```

#### BOLETA
```sql
CREATE TABLE boletas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_emision DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    estado VARCHAR(255) NOT NULL DEFAULT 'EMITIDA'
);
```

---

## 🔄 Flujo del Proyecto

### 1. **Creacion de Usuario**
```
Cliente → API Gateway (8085) → REGISTRO (8084)
                                      ↓
                              Crea en USUARIOS (8086)
```

**Descripción:**
- El usuario se registra con: nombre, email, password, dirección
- REGISTRO guarda todos los datos del usuario
- REGISTRO automáticamente crea credenciales en USUARIOS (solo email y password)
- Esto permite separar la información personal de las credenciales de acceso

**Endpoint:** `POST /api/registro/registrar`

**Request:**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "12345",
  "direccion": "Calle Principal 123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "direccion": "Calle Principal 123"
  }
}
```

---

### 2. **Inicio de Sesión**
```
Cliente → API Gateway (8085) → INGRESO (8080)
                                      ↓
                              Valida en USUARIOS (8086)
```

**Descripción:**
- El usuario ingresa email y password
- INGRESO consulta USUARIOS para validar credenciales
- Retorna confirmación de acceso exitoso

**Endpoint:** `POST /api/ingreso/login`

**Request:**
```json
{
  "email": "juan@example.com",
  "password": "12345"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "id": 1,
    "email": "juan@example.com"
  }
}
```

---

### 3. **Consulta de Productos**
```
Cliente → API Gateway (8085) → PRODUCTOS (8083)
```

**Descripción:**
- Catálogo completo de productos de la tienda
- Información de precio, stock, categoría y descripción
- Permite búsqueda y filtrado

**Endpoints:**
- `GET /api/productos/listar` - Listar todos los productos
- `GET /api/productos/{id}` - Detalle de un producto específico
- `POST /api/productos/crear` - Crear nuevo producto
- `PUT /api/productos/actualizar/{id}` - Actualizar producto
- `DELETE /api/productos/eliminar/{id}` - Eliminar producto

**Response (Listar):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nombre": "Teclado Gamer RGB",
      "precio": 89.99,
      "descripcion": "Teclado mecánico con iluminación RGB",
      "stock": 50,
      "categoria": "Periféricos"
    }
  ],
  "total": 1
}
```

---

### 4. **Realizar Venta**
```
Cliente → API Gateway (8085) → VENTAS (8087)
                                      ↓
                              Valida REGISTRO (8084)
                                      ↓
                              Valida PRODUCTOS (8083)
                                      ↓
                              Genera BOLETA (8088)
```

**Descripción:**
- El usuario realiza una compra
- VENTAS valida que el usuario existe en REGISTRO
- VENTAS obtiene precio y valida stock en PRODUCTOS
- Se registra la venta con cálculo automático del total
- Se genera automáticamente la boleta asociada

**Endpoint:** `POST /api/ventas/crear`

**Request:**
```json
{
  "usuarioId": 1,
  "productoId": 1,
  "cantidad": 2
}
```

**Response:**
```json
{
  "success": true,
  "message": "Venta creada y boleta generada exitosamente",
  "data": {
    "id": 1,
    "usuarioId": 1,
    "productoId": 1,
    "cantidad": 2,
    "precioUnitario": 89.99,
    "total": 179.98,
    "fecha": "2025-11-23T20:30:00",
    "boletaId": 1,
    "estado": "COMPLETADA",
    "usuario": {
      "id": 1,
      "nombre": "Juan Pérez",
      "email": "juan@example.com",
      "direccion": "Calle Principal 123"
    },
    "producto": {
      "id": 1,
      "nombre": "Teclado Gamer RGB",
      "precio": 89.99,
      "descripcion": "Teclado mecánico con iluminación RGB"
    }
  }
}
```

---

### 5. **Generación de Boleta (Automática)**
```
VENTAS (8087) → BOLETA (8088)
                      ↓
              Consulta VENTAS (8087)
                      ↓
              Consulta REGISTRO (8084)
```

**Descripción:**
- La boleta se genera **automáticamente** al crear una venta
- BOLETA obtiene información completa de la venta
- BOLETA obtiene información del usuario desde REGISTRO
- Se genera el documento con todos los detalles

**Endpoint:** `GET /api/boleta/{id}`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "ventaId": 1,
    "usuarioId": 1,
    "fechaEmision": "2025-11-23T20:30:00",
    "total": 179.98,
    "estado": "EMITIDA",
    "venta": {
      "id": 1,
      "cantidad": 2,
      "precioUnitario": 89.99,
      "total": 179.98,
      "fecha": "2025-11-23T20:30:00",
      "estado": "COMPLETADA"
    },
    "usuario": {
      "id": 1,
      "nombre": "Juan Pérez",
      "email": "juan@example.com",
      "direccion": "Calle Principal 123"
    }
  }
}
```

---

## 🔗 Comunicación entre Servicios

### Dependencias de Servicios

#### INGRESO (8080)
- **Consume:** USUARIOS (8086)
- **Propósito:** Validar credenciales de login

#### REGISTRO (8084)
- **Consume:** USUARIOS (8086)
- **Propósito:** Crear credenciales al registrar usuario

#### VENTAS (8087)
- **Consume:** 
  - REGISTRO (8084) - Validar usuario
  - PRODUCTOS (8083) - Obtener precio y validar stock
  - BOLETA (8088) - Generar boleta automáticamente

#### BOLETA (8088)
- **Consume:**
  - VENTAS (8087) - Obtener información de venta
  - REGISTRO (8084) - Obtener información del usuario

### Patrón de Comunicación: RestTemplate

Todos los servicios utilizan `RestTemplate` para comunicación HTTP síncrona:

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**Configuración en `application.properties`:**
```properties
# Ejemplo en VENTAS
servicio.productos.url=http://localhost:8083
servicio.registro.url=http://localhost:8084
servicio.boleta.url=http://localhost:8088
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.2.0**: Framework principal
- **Spring Data JPA**: Persistencia de datos
- **Spring Cloud Gateway**: API Gateway
- **Hibernate**: ORM
- **RestTemplate**: Comunicación entre microservicios

### Base de Datos
- **MySQL 8.0**: Base de datos relacional
- **HikariCP**: Connection pooling

### Documentación
- **SpringDoc OpenAPI 3 (Swagger)**: Documentación interactiva de APIs
- **Lombok**: Reducción de código boilerplate

### Build & Deployment
- **Maven**: Gestión de dependencias y build
- **Java 17**: Versión de Java

### Arquitectura
- **Microservices Pattern**: Arquitectura de microservicios
- **API Gateway Pattern**: Punto de entrada único
- **Database per Service Pattern**: Una base de datos compartida con tablas separadas

---

## 📦 Instalación y Configuración

### Prerrequisitos

1. **Java 17 o superior**
   ```bash
   java -version
   ```

2. **Maven 3.6 o superior**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0 con Laragon**
   - Iniciar Laragon
   - Verificar que MySQL esté corriendo en puerto 3306

4. **Crear Base de Datos**
   ```sql
   CREATE DATABASE IF NOT EXISTS tiendagamer;
   USE tiendagamer;
   ```

### Configuración de Bases de Datos

Cada servicio tiene su `application.properties` configurado:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tiendagamer
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Clonar el Proyecto

```bash
git clone https://github.com/tu-usuario/tienda-gamer.git
cd Tienda-Gamer
```

### Compilar todos los servicios

```bash
# Compilar cada microservicio
cd ingreso && mvn clean install && cd ..
cd productos && mvn clean install && cd ..
cd registro && mvn clean install && cd ..
cd tienda && mvn clean install && cd ..
cd usuarios && mvn clean install && cd ..
cd ventas && mvn clean install && cd ..
cd boleta/boleta && mvn clean install && cd ../..
```

---

## 🚀 Ejecución del Proyecto

### Orden de Inicio Recomendado

#### 1. **Servicios Base** (iniciar en este orden)

```bash
# Terminal 1 - REGISTRO
cd registro
mvn spring-boot:run

# Terminal 2 - USUARIOS
cd usuarios
mvn spring-boot:run

# Terminal 3 - PRODUCTOS
cd productos
mvn spring-boot:run
```

#### 2. **Servicios Dependientes**

```bash
# Terminal 4 - INGRESO (depende de USUARIOS)
cd ingreso
mvn spring-boot:run

# Terminal 5 - VENTAS (depende de USUARIOS, PRODUCTOS y BOLETA)
cd ventas
mvn spring-boot:run
```

#### 3. **Servicios de Facturación**

```bash
# Terminal 6 - BOLETA (depende de VENTAS y REGISTRO)
cd boleta/boleta
mvn spring-boot:run
```

#### 4. **API Gateway** (iniciar al final)

```bash
# Terminal 7 - TIENDA (Gateway)
cd tienda
mvn spring-boot:run
```

### Verificar que todos los servicios estén corriendo

```bash
# Verificar puertos en uso (Windows)
netstat -ano | findstr "8080 8083 8084 8085 8086 8087 8088"
```

### Acceder a la Documentación Swagger

Cada servicio tiene su propia documentación:

| Servicio | URL Swagger |
|----------|-------------|
| INGRESO | http://localhost:8080/doc/swagger-ui.html |
| PRODUCTOS | http://localhost:8083/doc/swagger-ui.html |
| REGISTRO | http://localhost:8084/doc/swagger-ui.html |
| USUARIOS | http://localhost:8086/doc/swagger-ui.html |
| VENTAS | http://localhost:8087/doc/swagger-ui.html |
| BOLETA | http://localhost:8088/doc/swagger-ui.html |

---

## 🌐 API Gateway - Rutas

**Todas las peticiones deben hacerse a través del puerto 8085 (TIENDA)**

### Tabla de Rutas

| Ruta Gateway | Servicio Destino | Puerto | Descripción |
|--------------|-----------------|--------|-------------|
| `/api/ingreso/**` | INGRESO | 8080 | Autenticación y login |
| `/api/productos/**` | PRODUCTOS | 8083 | Gestión de productos |
| `/api/registro/**` | REGISTRO | 8084 | Registro de usuarios |
| `/api/usuarios/**` | USUARIOS | 8086 | Gestión de credenciales |
| `/api/ventas/**` | VENTAS | 8087 | Procesamiento de ventas |
| `/api/boleta/**` | BOLETA | 8088 | Generación de boletas |

### Configuración del Gateway

```java
// GatewayConfig.java en TIENDA
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
            .route("ingreso", r -> r.path("/api/ingreso/**")
                    .uri("http://localhost:8080"))
            .route("productos", r -> r.path("/api/productos/**")
                    .uri("http://localhost:8083"))
            .route("registro", r -> r.path("/api/registro/**")
                    .uri("http://localhost:8084"))
            .route("usuarios", r -> r.path("/api/usuarios/**")
                    .uri("http://localhost:8086"))
            .route("ventas", r -> r.path("/api/ventas/**")
                    .uri("http://localhost:8087"))
            .route("boleta", r -> r.path("/api/boleta/**")
                    .uri("http://localhost:8088"))
            .build();
}
```

---

## 📝 Ejemplos de Uso

### Usando API Gateway (Recomendado)

#### 1. Registrar Usuario

```bash
POST http://localhost:8085/api/registro/registrar
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "12345",
  "direccion": "Calle Principal 123"
}
```

#### 2. Iniciar Sesión

```bash
POST http://localhost:8085/api/ingreso/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "12345"
}
```

#### 3. Crear Producto

```bash
POST http://localhost:8085/api/productos/crear
Content-Type: application/json

{
  "nombre": "Teclado Gamer RGB",
  "precio": 89.99,
  "descripcion": "Teclado mecánico con iluminación RGB",
  "stock": 50,
  "categoria": "Periféricos"
}
```

#### 4. Listar Productos

```bash
GET http://localhost:8085/api/productos/listar
```

#### 5. Crear Venta (genera boleta automáticamente)

```bash
POST http://localhost:8085/api/ventas/crear
Content-Type: application/json

{
  "usuarioId": 1,
  "productoId": 1,
  "cantidad": 2
}
```

#### 6. Ver Boletas

```bash
GET http://localhost:8085/api/boleta/listar
```

#### 7. Ver Boleta Específica

```bash
GET http://localhost:8085/api/boleta/1
```

#### 8. Ver Boletas por Usuario

```bash
GET http://localhost:8085/api/boleta/usuario/1
```

### Usando Servicios Directamente (Para Testing)

```bash
# Directamente a PRODUCTOS
GET http://localhost:8083/api/productos/listar

# Directamente a VENTAS
POST http://localhost:8087/api/ventas/crear

# Directamente a BOLETA
GET http://localhost:8088/api/boleta/listar
```

---

## ⚠️ Notas Importantes

### Dependencias Críticas

- ✅ **MySQL debe estar corriendo** antes de iniciar cualquier servicio
- ✅ **USUARIOS** debe iniciarse antes que INGRESO y REGISTRO
- ✅ **PRODUCTOS** debe iniciarse antes que VENTAS
- ✅ **REGISTRO** debe iniciarse antes que VENTAS y BOLETA
- ✅ **VENTAS y BOLETA** deben estar corriendo para el flujo completo
- ✅ **TIENDA (Gateway)** debe iniciarse al final

### Arquitectura de Datos

📊 **Base de Datos Compartida**: Todos los servicios usan `tiendagamer` pero con tablas separadas

```
tiendagamer
├── registro    (REGISTRO)
├── usuarios    (USUARIOS)
├── productos   (PRODUCTOS)
├── ventas      (VENTAS)
└── boletas     (BOLETA)
```

### Flujo Automático

🔄 **Registro → Usuarios**: Al registrarse, se crea automáticamente el usuario con credenciales

🔄 **Venta → Boleta**: Al crear una venta, se genera automáticamente la boleta asociada

### Seguridad

🔒 **Contraseñas**: En esta versión las contraseñas se guardan en texto plano (NO usar en producción)

🔐 **Autenticación**: El servicio INGRESO valida credenciales pero no implementa JWT

### Swagger UI

📖 **Documentación Interactiva**: Cada servicio tiene su propia documentación en `/doc/swagger-ui.html`

🎯 **Testing**: Puedes probar todos los endpoints directamente desde Swagger


### Base de datos no encontrada

```sql
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS tiendagamer;

-- Verificar tablas
USE tiendagamer;
SHOW TABLES;
```
### Servicio no responde

1. Verificar que el servicio esté corriendo
2. Revisar logs en la consola
3. Verificar que el puerto esté libre
4. Confirmar que las dependencias estén iniciadas

### Gateway no redirige

1. Verificar que TIENDA esté en puerto 8085
2. Confirmar que todos los servicios estén corriendo
3. Revisar configuración en `ServicesProperties.java`

---

## 📚 Recursos Adicionales

### Herramientas Recomendadas

- **Postman**: Testing de APIs
- **Thunder Client**: Extensión de VS Code para testing
- **HeidiSQL**: Cliente MySQL incluido en Laragon
- **DBeaver**: Cliente universal de bases de datos

### Extensiones VS Code

- Spring Boot Extension Pack
- Java Extension Pack
- REST Client
- Thunder Client

--

## ✨ Características Futuras

- [ ] Implementar JWT para autenticación
- [ ] Agregar sistema de roles y permisos
- [ ] Implementar paginación en listados
- [ ] Agregar filtros y búsqueda avanzada
- [ ] Agregar logging centralizado
- [ ] Dockerizar todos los servicios
- [ ] Implementar CI/CD

**Última actualización**: Noviembre 2025
**Versión**: 7.7.7

