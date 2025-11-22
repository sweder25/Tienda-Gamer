README

## Arquitectura de Microservicios

Este proyecto implementa una tienda gamer utilizando arquitectura de microservicios con Spring Boot.

---

## Puertos de Servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| INGRESO | 8080 | Autenticación y login de usuarios |
| PRODUCTOS | 8083 | Gestión de productos de la tienda |
| REGISTRO | 8084 | Registro de nuevos usuarios |
| TIENDA (API Gateway) | 8085 | Punto de entrada único para todos los servicios |
| USUARIOS | 8086 | Gestión de información de usuarios |
| VENTAS | 8087 | Procesamiento de ventas |
| BOLETA | 8088 | Generación de boletas/facturas |

---

## Flujo del Proyecto

### 1. **Creacion de Usuario**
```
Cliente → API Gateway (8085) → Servicio Usuarios (8086)
```
- Se crea Usuario
- Se guarda en la base de datos del servicio Usuario
- Se crea automaticamente el registro con la fecha cuando se creo el usuario en el servicio Registro.
- **Endpoint:** `POST /api/usuarios/`

### 2. **Inicio de Sesión**
```
Cliente → API Gateway (8085) → Servicio INGRESO (8080) → Servicio USUARIOS (8086)
```
- El usuario ingresa credenciales
- INGRESO consume el servicio USUARIOS para validar
- Retorna token/sesión de autenticación
- **Endpoint:** `POST /api/ingreso/login`

### 3. **Consulta de Productos**
```
Cliente → API Gateway (8085) → Servicio PRODUCTOS (8083)
```
- El usuario navega por el catálogo
- Consulta información, precios y disponibilidad
- **Endpoints:**
  - `GET /api/producto/` - Listar todos
  - `GET /api/producto/{id}` - Detalle de producto

### 4. **Realizar Venta**
```
Cliente → API Gateway (8085) → Servicio VENTAS (8087)
                                      ↓
                              Consulta USUARIOS (8086)
                                      ↓
                              Consulta PRODUCTOS (8083)
```
- El usuario realiza una compra
- VENTAS valida el usuario con servicio USUARIOS
- VENTAS valida productos y stock con servicio PRODUCTOS
- Se registra la venta en la base de datos
- **Endpoint:** `POST /api/ventas/`
- **Request:** Incluye ID de usuario y lista de productos

### 5. **Generación de Boleta**
```
Cliente → API Gateway (8085) → Servicio BOLETA (8088)
                                      ↓
                              Consulta VENTAS (8087)
                                      ↓
                              Consulta USUARIOS (8086)
```
- Después de una venta exitosa, se genera la boleta
- BOLETA obtiene información de la venta desde VENTAS
- BOLETA obtiene información del usuario desde USUARIOS
- Se genera el documento de la boleta
- **Endpoint:** `POST /api/boleta/`

---



## Comunicación entre Servicios

### RestTemplate
Todos los servicios utilizan `RestTemplate` para comunicación síncrona:

- **INGRESO** → **USUARIOS**: Validación de credenciales
- **VENTAS** → **USUARIOS**: Validación de usuario en venta
- **VENTAS** → **PRODUCTOS**: Validación de productos y stock
- **BOLETA** → **VENTAS**: Obtener información de venta
- **BOLETA** → **USUARIOS**: Obtener información de usuario

### Configuración
Cada servicio tiene `RestTemplateConfig.java` para configurar la comunicación HTTP.

---

## Tecnologías Utilizadas

- **Spring Boot**: Framework principal
- **Spring Data JPA**: Persistencia de datos
- **RestTemplate**: Comunicación entre microservicios
- **Swagger**: Documentación de APIs
- **Maven**: Gestión de dependencias
- **API Gateway Pattern**: Punto de entrada único

---

## Ejecución del Proyecto

### Orden de Inicio Recomendado:

1. **Servicios Base** (pueden iniciarse en paralelo):
   - REGISTRO (8084)
   - USUARIOS (8086)
   - PRODUCTOS (8083)

2. **Servicios Dependientes**:
   - INGRESO (8080) - depende de USUARIOS
   - VENTAS (8087) - depende de USUARIOS y PRODUCTOS

3. **Servicios de Facturación**:
   - BOLETA (8088) - depende de VENTAS y USUARIOS

4. **API Gateway**:
   - TIENDA (8085) - último en iniciar

### Comando para cada servicio:
```bash
cd [nombre-servicio]
mvn spring-boot:run
```

---

## Extensiones Recomendadas

- **POSTMAN**: Pruebas de APIs individuales
- **THUNDER CLIENT**: Pruebas a través del API Gateway

---

## Rutas del API Gateway

Todas las peticiones deben hacerse a través del puerto **8085**:

| Ruta | Servicio Destino | Puerto |
|------|-----------------|--------|
| `/api/ingreso/**` | INGRESO | 8080 |
| `/api/producto/**` | PRODUCTOS | 8083 |
| `/api/registro/**` | REGISTRO | 8084 |
| `/api/usuarios/**` | USUARIOS | 8086 |
| `/api/ventas/**` | VENTAS | 8087 |
| `/api/boleta/**` | BOLETA | 8088 |

**Ejemplo de uso:**
```
http://localhost:8085/api/producto/listar
http://localhost:8085/api/ingreso/login
http://localhost:8085/api/ventas/crear
```

---

## Notas Importantes

⚠️ **IMPORTANTE**: Todos los servicios deben estar ejecutándose en sus respectivos puertos para que el API Gateway funcione correctamente.

📝 **Base de Datos**: Cada microservicio maneja su propia base de datos (patrón Database per Service).

🔒 **Seguridad**: El servicio INGRESO maneja la autenticación, pero no se implementa JWT en esta versión.
