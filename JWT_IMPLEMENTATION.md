# Implementación JWT en Tienda-Gamer

## 📋 Resumen

Se ha implementado autenticación JWT (JSON Web Token) en la arquitectura de microservicios de Tienda-Gamer.

## 🏗️ Arquitectura

### Microservicio de Autenticación: **ingreso** (Puerto 8080)

- **Responsabilidad**: Genera y valida tokens JWT
- **Endpoints**:
  - `POST /api/auth/register` - Registra un nuevo usuario
  - `POST /api/auth/login` - Autentica y genera token JWT
  - `GET /api/auth/validate` - Valida un token JWT

### Microservicios Protegidos

Los siguientes microservicios validan tokens JWT en todas sus rutas `/api/**`:
- **productos** (Puerto 8083)
- Puedes agregar: **ventas**, **boleta**, **tienda**, **usuarios**, **registro**

## 🚀 Cómo usar

### 1. Registrar un usuario

```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "miPassword123",
  "nombre": "Juan Pérez",
  "rut": 12345678,
  "rol": "USER"
}
```

**Respuesta**:
```json
{
  "message": "Usuario registrado exitosamente",
  "email": "usuario@example.com"
}
```

### 2. Hacer Login

```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "miPassword123"
}
```

**Respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3Vhcmlv...",
  "email": "usuario@example.com",
  "message": "Login exitoso"
}
```

### 3. Usar el token en otros microservicios

Para acceder a rutas protegidas (como productos), incluye el token en el header:

```bash
GET http://localhost:8083/api/productos
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3Vhcmlv...
```

### 4. Validar un token

```bash
GET http://localhost:8080/api/auth/validate
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c3Vhcmlv...
```

**Respuesta**:
```json
{
  "valid": true
}
```

## 🔒 Configuración de Seguridad

### Secret Key (jwt.secret)

**IMPORTANTE**: Todos los microservicios deben usar la **misma clave secreta** configurada en `application.properties`:

```properties
jwt.secret=TiendaGamer2025SecretKeyForJWTTokenGenerationVerySecure123456789
```

### Expiración del Token (jwt.expiration)

Por defecto: 24 horas (86400000 ms)

```properties
jwt.expiration=86400000
```

## 📦 Para agregar JWT a otros microservicios

### Paso 1: Agregar dependencias al pom.xml

```xml
<!-- JWT para validación de tokens -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
```

### Paso 2: Copiar archivos

Copia estos archivos del microservicio **productos** a tu microservicio:

1. `config/JwtValidator.java`
2. `interceptor/JwtInterceptor.java`
3. `config/WebConfig.java`

### Paso 3: Agregar configuración

En `application.properties`:

```properties
jwt.secret=TiendaGamer2025SecretKeyForJWTTokenGenerationVerySecure123456789
```

### Paso 4: Ajustar paquetes

Cambia los imports y nombres de paquetes según tu microservicio.

## 🧪 Pruebas con Swagger

Los endpoints de Swagger están excluidos de la autenticación:
- `http://localhost:8080/doc/swagger-ui.html` (ingreso)
- `http://localhost:8083/doc/swagger-ui.html` (productos)

Para probar endpoints protegidos en Swagger:
1. Haz login en `/api/auth/login`
2. Copia el token
3. Click en "Authorize" en Swagger UI
4. Ingresa: `Bearer <tu-token>`

## ⚠️ Notas Importantes

1. **Contraseñas**: Se encriptan con BCrypt automáticamente
2. **CORS**: Asegúrate de configurar CORS si tienes frontend
3. **Secret Key**: En producción, usa variables de entorno
4. **Base de Datos**: Los usuarios se guardan en la tabla `usuarios`

## 🔧 Troubleshooting

### Error: "Token no proporcionado"
- Verifica que el header `Authorization` tenga formato: `Bearer <token>`

### Error: "Token inválido o expirado"
- El token expiró (genera uno nuevo con `/api/auth/login`)
- La secret key es diferente entre microservicios

### Error: "Usuario no encontrado"
- Registra el usuario primero con `/api/auth/register`

## 📚 Recursos

- [JWT.io](https://jwt.io/) - Decodificar y verificar tokens
- [JJWT Library](https://github.com/jwtk/jjwt) - Documentación de la librería

---

✅ **JWT implementado exitosamente en Tienda-Gamer**
