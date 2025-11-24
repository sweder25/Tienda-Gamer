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
        └─────────────┘   └─────────────┘  └────┬───────┘
               │                                  │
        ┌──────▼──────┐                    ┌─────▼──────┐
        │PRODUCTOS:8083│◄───────────────────┤VENTAS:8087 │
        └─────────────┘                    └────────────┘
```

---

## 🔌 Puertos de Servicios

| Servicio | Puerto | Descripción | Base de Datos |
|----------|--------|-------------|---------------|
| **INGRESO** | 8080 | Autenticación y login de usuarios | `tienda_gamer` |
| **PRODUCTOS** | 8083 | Gestión del catálogo de productos | `tienda_gamer` |
| **REGISTRO** | 8084 | Registro de nuevos usuarios | `tienda_gamer` |
| **TIENDA** (Gateway) | 8085 | Punto de entrada único - API Gateway | N/A |
| **USUARIOS** | 8086 | Gestión de credenciales (email/password) | `tienda_gamer` |
| **VENTAS** | 8087 | Procesamiento y registro de ventas | `tienda_gamer` |
| **BOLETA** | 8088 | Generación automática de boletas electrónicas | `tienda_gamer` |

---

## 🗄️ Bases de Datos

### Base de Datos Compartida: `tienda_gamer`

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
    total DOUBLE NOT NULL,
    fecha DATETIME NOT NULL,
    nombre_cliente VARCHAR(255) NOT NULL,
    email_cliente VARCHAR(255) NOT NULL,
    direccion VARCHAR(500),
    metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'COMPLETADA'
);

CREATE TABLE venta_detalles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id)
);
```

#### BOLETA
```sql
CREATE TABLE boletas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_boleta VARCHAR(100) NOT NULL UNIQUE,
    venta_id BIGINT NOT NULL,
    usuario_id BIGINT,
    fecha_emision DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    nombre_cliente VARCHAR(255) NOT NULL,
    email_cliente VARCHAR(255) NOT NULL,
    direccion_envio VARCHAR(500),
    metodo_pago VARCHAR(50) NOT NULL DEFAULT 'NO_ESPECIFICADO',
    estado VARCHAR(50) DEFAULT 'EMITIDA',
    detalle_productos TEXT
);
```

---

## 🔄 Flujo del Proyecto

### 1. **Creación de Usuario**
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

### 4. **Realizar Venta (Carrito Completo)**
```
Cliente → API Gateway (8085) → VENTAS (8087)
                                      ↓
                              Valida REGISTRO (8084)
                                      ↓
                              Valida PRODUCTOS (8083)
                                      ↓
                              Crea Venta con Detalles
                                      ↓
                              Genera BOLETA (8088) automáticamente
```

**Descripción:**
- El usuario realiza una compra con múltiples productos
- VENTAS valida que el usuario existe en REGISTRO
- VENTAS obtiene precio y valida stock para cada producto
- Se registra la venta principal más los detalles de cada producto
- Calcula el total sumando todos los subtotales
- Se genera automáticamente la boleta electrónica asociada

**Endpoint:** `POST /api/ventas/crear`

**Request:**
```json
{
  "usuarioId": 1,
  "metodoPago": "TARJETA_CREDITO",
  "direccion": "Calle Principal 123",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 2,
      "cantidad": 1
    }
  ]
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
    "total": 279.97,
    "fecha": "2025-11-24T00:30:00",
    "nombreCliente": "Juan Pérez",
    "emailCliente": "juan@example.com",
    "direccion": "Calle Principal 123",
    "metodoPago": "TARJETA_CREDITO",
    "estado": "COMPLETADA",
    "detalles": [
      {
        "id": 1,
        "productoId": 1,
        "nombreProducto": "Teclado Gamer RGB",
        "cantidad": 2,
        "precioUnitario": 89.99,
        "subtotal": 179.98
      },
      {
        "id": 2,
        "productoId": 2,
        "nombreProducto": "Mouse Gamer",
        "cantidad": 1,
        "precioUnitario": 99.99,
        "subtotal": 99.99
      }
    ]
  }
}
```

---

### 5. **Generación de Boleta Electrónica (Automática)**
```
VENTAS (8087) → BOLETA (8088)
                      ↓
              Consulta datos de VENTA
                      ↓
              Genera número de boleta único
                      ↓
              Guarda boleta con detalles completos
```

**Descripción:**
- La boleta se genera **automáticamente** al crear una venta exitosa
- BOLETA obtiene toda la información de la venta (productos, cantidades, precios)
- Se genera un número de boleta único: `BOL-{timestamp}-{UUID}`
- Incluye toda la información del cliente y detalles de productos
- Estado inicial: "EMITIDA"

**Endpoints:**
- `GET /api/boletas` - Listar todas las boletas
- `GET /api/boletas/{id}` - Obtener boleta por ID
- `GET /api/boletas/venta/{ventaId}` - Obtener boleta por ID de venta
- `POST /api/boletas` - Generar boleta manualmente (opcional)

**Response (Obtener Boleta):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "numero": "BOL-1732410000000-A1B2",
    "ventaId": 1,
    "usuarioId": 1,
    "fechaEmision": "2025-11-24T00:30:00",
    "total": 279.97,
    "nombreCliente": "Juan Pérez",
    "emailCliente": "juan@example.com",
    "direccionEnvio": "Calle Principal 123",
    "metodoPago": "TARJETA_CREDITO",
    "estado": "EMITIDA",
    "detalleProductos": "[{\"productoId\":1,\"cantidad\":2,\"precioUnitario\":89.99,\"subtotal\":179.98}]"
  }
}
```

**Response (Listar Todas):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "numero": "BOL-1732410000000-A1B2",
      "ventaId": 1,
      "usuarioId": 1,
      "fechaEmision": "2025-11-24T00:30:00",
      "total": 279.97,
      "nombreCliente": "Juan Pérez",
      "emailCliente": "juan@example.com",
      "estado": "EMITIDA"
    }
  ],
  "total": 1
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
  - REGISTRO (8084) - Obtener información completa del usuario
  - PRODUCTOS (8083) - Obtener precio y validar stock de cada producto
  - BOLETA (8088) - Generar boleta automáticamente tras completar venta

#### BOLETA (8088)
- **Consume:**
  - VENTAS (8087) - Obtener información completa de la venta y sus detalles

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

# Ejemplo en BOLETA
servicio.ventas.url=http://localhost:8087
servicio.registro.url=http://localhost:8084
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.7**: Framework principal
- **Spring Data JPA**: Persistencia de datos
- **Spring Cloud Gateway**: API Gateway
- **Hibernate**: ORM
- **RestTemplate**: Comunicación entre microservicios
- **Lombok**: Reducción de código boilerplate

### Base de Datos
- **MySQL 8.0**: Base de datos relacional
- **HikariCP**: Connection pooling

### Documentación
- **SpringDoc OpenAPI 3 (Swagger)**: Documentación interactiva de APIs

### Build & Deployment
- **Maven**: Gestión de dependencias y build
- **Java 17**: Versión de Java

### Arquitectura
- **Microservices Pattern**: Arquitectura de microservicios
- **API Gateway Pattern**: Punto de entrada único
- **Database per Service Pattern**: Una base de datos compartida con tablas separadas por servicio

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
   CREATE DATABASE IF NOT EXISTS tienda_gamer;
   USE tienda_gamer;
   ```

### Configuración de Bases de Datos

Cada servicio tiene su `application.properties` configurado:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tienda_gamer?useSSL=false&serverTimezone=UTC
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
cd boleta && mvn clean install && cd ..
```

---

## 🚀 Ejecución del Proyecto

### Orden de Inicio Recomendado

#### 1. **Servicios Base** (iniciar en este orden)

```bash
# Terminal 1 - USUARIOS (debe iniciar primero)
cd usuarios
mvn spring-boot:run

# Terminal 2 - REGISTRO
cd registro
mvn spring-boot:run

# Terminal 3 - PRODUCTOS
cd productos
mvn spring-boot:run
```

#### 2. **Servicios de Negocio**

```bash
# Terminal 4 - INGRESO (depende de USUARIOS)
cd ingreso
mvn spring-boot:run

# Terminal 5 - BOLETA (debe iniciar antes que VENTAS)
cd boleta
mvn spring-boot:run

# Terminal 6 - VENTAS (depende de USUARIOS, PRODUCTOS y BOLETA)
cd ventas
mvn spring-boot:run
```

#### 3. **API Gateway** (iniciar al final)

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
| `/api/boletas/**` | BOLETA | 8088 | Generación y consulta de boletas |

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
            .route("boletas", r -> r.path("/api/boletas/**")
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

#### 5. Crear Venta con Carrito Completo (genera boleta automáticamente)

```bash
POST http://localhost:8085/api/ventas/crear
Content-Type: application/json

{
  "usuarioId": 1,
  "metodoPago": "TARJETA_CREDITO",
  "direccion": "Calle Principal 123",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 2,
      "cantidad": 1
    }
  ]
}
```

#### 6. Listar Todas las Boletas

```bash
GET http://localhost:8085/api/boletas
```

#### 7. Ver Boleta Específica por ID

```bash
GET http://localhost:8085/api/boletas/1
```

#### 8. Ver Boleta por ID de Venta

```bash
GET http://localhost:8085/api/boletas/venta/1
```

#### 9. Listar Ventas de un Usuario

```bash
GET http://localhost:8085/api/ventas/usuario/1
```

### Usando Servicios Directamente (Para Testing)

```bash
# Directamente a PRODUCTOS
GET http://localhost:8083/api/productos/listar

# Directamente a VENTAS
POST http://localhost:8087/api/ventas/crear

# Directamente a BOLETA
GET http://localhost:8088/api/boletas
GET http://localhost:8088/api/boletas/1
GET http://localhost:8088/api/boletas/venta/1
```

---

## ⚠️ Notas Importantes

### Dependencias Críticas

- ✅ **MySQL debe estar corriendo** antes de iniciar cualquier servicio
- ✅ **USUARIOS** debe iniciarse primero (puerto 8086)
- ✅ **REGISTRO** debe iniciarse después de USUARIOS
- ✅ **PRODUCTOS** debe estar corriendo antes de VENTAS
- ✅ **BOLETA** debe iniciarse antes de VENTAS
- ✅ **VENTAS** depende de REGISTRO, PRODUCTOS y BOLETA
- ✅ **TIENDA (Gateway)** debe iniciarse al final

### Arquitectura de Datos

📊 **Base de Datos Compartida**: Todos los servicios usan `tienda_gamer` pero con tablas separadas

```
tienda_gamer
├── registro           (REGISTRO)
├── usuarios           (USUARIOS)
├── productos          (PRODUCTOS)
├── ventas             (VENTAS)
├── venta_detalles     (VENTAS - detalles de productos)
└── boletas            (BOLETA)
```

### Flujo Automático

🔄 **Registro → Usuarios**: Al registrarse, se crea automáticamente el usuario con credenciales

🔄 **Venta → Boleta**: Al crear una venta, se genera automáticamente la boleta electrónica asociada

🔄 **Venta → Detalles**: Cada venta tiene múltiples detalles (uno por producto en el carrito)

### Características de las Boletas

📄 **Número Único**: Cada boleta tiene un número único formato `BOL-{timestamp}-{UUID}`

💰 **Total Calculado**: El total se calcula sumando todos los subtotales de los productos

📋 **Detalles en JSON**: Los detalles de productos se guardan en formato JSON para facilitar la consulta

👤 **Información Completa**: Incluye datos del cliente, dirección, método de pago y estado

### Seguridad

🔒 **Contraseñas**: En esta versión las contraseñas se guardan en texto plano (NO usar en producción)

🔐 **Autenticación**: El servicio INGRESO valida credenciales pero no implementa JWT

⚠️ **CORS**: Habilitado para desarrollo (`@CrossOrigin(origins = "*")`)

### Swagger UI

📖 **Documentación Interactiva**: Cada servicio tiene su propia documentación en `/doc/swagger-ui.html`

🎯 **Testing**: Puedes probar todos los endpoints directamente desde Swagger

### Troubleshooting Común

#### Base de datos no encontrada

```sql
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS tienda_gamer;

-- Verificar tablas
USE tienda_gamer;
SHOW TABLES;

-- Verificar estructura de boletas
DESCRIBE boletas;
```

#### Servicio no responde

1. Verificar que el servicio esté corriendo
2. Revisar logs en la consola (buscar errores en rojo)
3. Verificar que el puerto esté libre
4. Confirmar que las dependencias estén iniciadas

#### Gateway no redirige

1. Verificar que TIENDA esté en puerto 8085
2. Confirmar que todos los servicios estén corriendo
3. Revisar configuración en `ServicesProperties.java`

#### Campo 'total' no se ve en boletas

1. Verificar que la columna `total` existe en la tabla `boletas`
2. Revisar logs del servicio BOLETA al generar boletas
3. Confirmar que `@Column(name = "total")` está en el modelo Boleta
4. Verificar que se use `setTotal()` y no `setMonto()` en el service

---

## 🎯 Modelo de Datos Detallado

### Venta
```java
{
  "id": Long,
  "usuarioId": Long,
  "total": Double,              // Suma de todos los subtotales
  "fecha": LocalDateTime,
  "nombreCliente": String,
  "emailCliente": String,
  "direccion": String,
  "metodoPago": String,         // TARJETA_CREDITO, TRANSFERENCIA, etc.
  "estado": String,             // COMPLETADA, PENDIENTE, CANCELADA
  "detalles": [                 // Array de productos
    {
      "id": Long,
      "ventaId": Long,
      "productoId": Long,
      "cantidad": Integer,
      "precioUnitario": Double,
      "subtotal": Double
    }
  ]
}
```

### Boleta
```java
{
  "id": Long,
  "numero": String,             // BOL-{timestamp}-{UUID}
  "ventaId": Long,
  "usuarioId": Long,
  "fechaEmision": LocalDateTime,
  "total": Double,              // Copia del total de la venta
  "nombreCliente": String,
  "emailCliente": String,
  "direccionEnvio": String,
  "metodoPago": String,
  "estado": String,             // EMITIDA, ANULADA
  "detalleProductos": String    // JSON con array de productos
}
