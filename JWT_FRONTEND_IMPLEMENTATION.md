# Implementación de JWT en Frontend React

## 📋 Resumen de Cambios

Se ha implementado autenticación JWT completa en el frontend de Tienda-Gamer. Todos los servicios ahora incluyen automáticamente el token JWT en las peticiones a los microservicios.

## 🔧 Archivos Creados

### 1. `/src/utils/authUtils.js`
Utilidad central para manejo de tokens JWT:
- `setToken(token)` - Guardar token en localStorage
- `getToken()` - Obtener token actual
- `removeToken()` - Eliminar token
- `hasToken()` - Verificar si existe token
- `setUser(user)` - Guardar datos del usuario
- `getUser()` - Obtener datos del usuario
- `clearAuth()` - Limpiar todo (logout completo)
- `getAuthHeaders()` - Obtener headers con Authorization
- `isTokenExpired()` - Verificar si el token expiró
- `isAuthenticated()` - Verificar autenticación completa

### 2. `/src/utils/fetchWithAuth.js`
Helpers para peticiones con autenticación automática:
- `fetchWithAuth(url, options)` - Wrapper de fetch con JWT
- `getWithAuth(url)` - GET con autenticación
- `postWithAuth(url, data)` - POST con autenticación
- `putWithAuth(url, data)` - PUT con autenticación
- `deleteWithAuth(url)` - DELETE con autenticación

**Maneja automáticamente:**
- Agregar token JWT a headers
- Detectar errores 401 (token inválido/expirado)
- Redirigir a login cuando es necesario
- Limpiar localStorage en caso de error de autenticación

## 📝 Archivos Modificados

### 1. `/src/API/IngresoService.jsx`
**Cambios principales:**
- ✅ Endpoint cambiado de `/api/ingreso` a `/api/auth`
- ✅ Método `login()` ahora guarda el token JWT
- ✅ Nuevo método `register()` para registro con JWT
- ✅ Método `logout()` limpia token y usuario
- ✅ Nuevo método `validarToken()` para verificar token con backend

**Endpoints actualizados:**
```javascript
POST http://localhost:8080/api/auth/login
POST http://localhost:8080/api/auth/register
GET  http://localhost:8080/api/auth/validate
```

### 2. `/src/context/AuthContext.jsx`
**Cambios principales:**
- ✅ Agregado método `register()` al contexto
- ✅ `useEffect` ahora verifica si el token expiró
- ✅ `isAuthenticated` valida tanto usuario como token
- ✅ Auto-limpieza si el token está expirado

**Nuevas funciones disponibles:**
```javascript
const { usuario, login, register, logout, isAuthenticated, loading } = useAuth();
```

### 3. `/src/API/productoService.jsx`
**Cambios principales:**
- ✅ Todas las peticiones incluyen `authUtils.getAuthHeaders()`
- ✅ GET, POST, PUT, DELETE con token JWT

### 4. `/src/API/ventaService.jsx`
**Cambios principales:**
- ✅ `crearVenta()` incluye token JWT
- ✅ `obtenerVentasPorUsuario()` incluye token JWT
- ✅ `obtenerVentaPorId()` incluye token JWT

### 5. `/src/API/boletaService.jsx`
**Cambios principales:**
- ✅ Todas las peticiones incluyen token JWT
- ✅ `obtenerTodas()`, `obtenerPorId()`, `obtenerPorVentaId()`, `generarBoleta()`

### 6. `/src/API/registroService.jsx`
**Cambios principales:**
- ✅ Todas las peticiones incluyen token JWT
- ✅ `registrarUsuario()` y `verificarRut()` con autenticación

### 7. `/src/API/tiendaService.jsx`
**Cambios principales:**
- ✅ Interceptor de Axios para agregar token automáticamente
- ✅ Interceptor de respuesta para manejar errores 401
- ✅ Redirección automática a login si token es inválido

**Interceptores configurados:**
```javascript
// Request interceptor - agrega token
axios.interceptors.request.use((config) => {
    const token = authUtils.getToken();
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

// Response interceptor - maneja 401
axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            authUtils.clearAuth();
            window.location.href = '/inicio';
        }
        return Promise.reject(error);
    }
);
```

### 8. `/src/pages/registro.jsx`
**Cambios principales:**
- ✅ Usa `useAuth()` en lugar de `registroService`
- ✅ Llama a `register()` del contexto
- ✅ Auto-login si el backend devuelve token
- ✅ Redirección al home si está autenticado

## 🔐 Flujo de Autenticación

### Login
1. Usuario ingresa email y password
2. `inicio.jsx` llama a `login(credenciales)`
3. `AuthContext` llama a `ingresoService.login()`
4. Backend responde con `{ token, usuario }`
5. Token se guarda en localStorage (`jwt_token`)
6. Usuario se guarda en localStorage (`usuario`)
7. Usuario es redirigido al home

### Registro
1. Usuario completa formulario de registro
2. `registro.jsx` llama a `register(userData)`
3. `AuthContext` llama a `ingresoService.register()`
4. Backend responde con `{ token, usuario }` (opcional)
5. Si hay token, usuario queda autenticado automáticamente
6. Redirección al home o a login según respuesta

### Peticiones Protegidas
1. Cualquier servicio hace una petición (productos, ventas, etc.)
2. `authUtils.getAuthHeaders()` obtiene el token
3. Se agrega header: `Authorization: Bearer <token>`
4. Si backend responde 401:
   - Token es inválido o expiró
   - Se limpia localStorage
   - Usuario es redirigido a `/inicio`

### Logout
1. Usuario hace click en logout
2. `logout()` llama a `ingresoService.logout()`
3. Se ejecuta `authUtils.clearAuth()`
4. Se eliminan `jwt_token` y `usuario` de localStorage
5. Usuario es redirigido a `/inicio`

## 🚀 Uso en Componentes

### Verificar si usuario está autenticado
```javascript
import { useAuth } from '../context/AuthContext';

function MiComponente() {
  const { isAuthenticated, usuario, loading } = useAuth();
  
  if (loading) return <div>Cargando...</div>;
  
  if (!isAuthenticated) {
    return <div>Debes iniciar sesión</div>;
  }
  
  return <div>Bienvenido {usuario.nombre}</div>;
}
```

### Hacer login
```javascript
import { useAuth } from '../context/AuthContext';

function Login() {
  const { login } = useAuth();
  
  const handleLogin = async () => {
    try {
      await login({ email: 'user@example.com', password: '123456' });
      // Login exitoso
    } catch (error) {
      console.error(error.message);
    }
  };
}
```

### Hacer logout
```javascript
import { useAuth } from '../context/AuthContext';

function Navbar() {
  const { logout } = useAuth();
  
  return (
    <button onClick={logout}>Cerrar Sesión</button>
  );
}
```

### Registrar nuevo usuario
```javascript
import { useAuth } from '../context/AuthContext';

function Registro() {
  const { register } = useAuth();
  
  const handleRegister = async (userData) => {
    try {
      await register(userData);
      // Registro exitoso
    } catch (error) {
      console.error(error.message);
    }
  };
}
```

## 🔍 Debugging

### Ver token actual
```javascript
import { authUtils } from '../utils/authUtils';

console.log('Token:', authUtils.getToken());
console.log('Usuario:', authUtils.getUser());
console.log('¿Autenticado?:', authUtils.isAuthenticated());
console.log('¿Token expirado?:', authUtils.isTokenExpired());
```

### Ver headers de petición
```javascript
import { authUtils } from '../utils/authUtils';

console.log('Headers:', authUtils.getAuthHeaders());
// Output: { 'Content-Type': 'application/json', 'Authorization': 'Bearer eyJ...' }
```

## ⚠️ Consideraciones Importantes

1. **Token Expiration**: El token JWT expira en 24 horas (configurado en backend)
2. **Auto-redirect**: Si una petición retorna 401, el usuario es redirigido automáticamente a login
3. **LocalStorage**: Token y usuario se guardan en localStorage (persiste entre recargas)
4. **CORS**: Asegúrate de que los backends permitan el header `Authorization`
5. **Rutas Protegidas**: Ya están configuradas en `route.jsx` con `ProtectedRoute`

## 📦 Endpoints del Backend

Asegúrate de que estos endpoints estén activos en el backend:

| Servicio | Puerto | Endpoint | Requiere JWT |
|----------|--------|----------|--------------|
| Ingreso (Auth) | 8080 | POST /api/auth/login | ❌ |
| Ingreso (Auth) | 8080 | POST /api/auth/register | ❌ |
| Ingreso (Auth) | 8080 | GET /api/auth/validate | ✅ |
| Productos | 8083 | GET/POST/PUT/DELETE /api/productos | ✅ |
| Ventas | 8087 | GET/POST /api/ventas | ✅ |
| Boletas | 8088 | GET/POST /api/boletas | ✅ |
| Tienda | 8085 | GET /api/tienda/* | ✅ |
| Registro | 8084 | POST /api/registro/* | ✅ |

## ✅ Checklist de Implementación

- [x] ✅ Crear `authUtils.js` con funciones de manejo de tokens
- [x] ✅ Crear `fetchWithAuth.js` con helpers de peticiones
- [x] ✅ Actualizar `IngresoService` para endpoints JWT
- [x] ✅ Actualizar `AuthContext` con soporte de tokens
- [x] ✅ Actualizar `productoService` con headers JWT
- [x] ✅ Actualizar `ventaService` con headers JWT
- [x] ✅ Actualizar `boletaService` con headers JWT
- [x] ✅ Actualizar `registroService` con headers JWT
- [x] ✅ Configurar interceptores de Axios en `tiendaService`
- [x] ✅ Actualizar página de registro para usar auth context
- [x] ✅ Manejar errores 401 automáticamente

## 🧪 Próximos Pasos

1. **Probar el flujo completo:**
   - Registrar un nuevo usuario
   - Iniciar sesión
   - Ver productos (debe incluir token)
   - Crear una venta (debe incluir token)
   - Cerrar sesión

2. **Verificar en DevTools:**
   - Abrir Network tab
   - Ver headers de peticiones
   - Confirmar que incluyen `Authorization: Bearer <token>`

3. **Probar expiración:**
   - Modificar token manualmente en localStorage
   - Hacer una petición
   - Verificar redirección a login

## 🛠️ Troubleshooting

### Problema: "Token inválido o expirado"
**Solución:** 
- Verificar que el token en localStorage sea correcto
- Asegurarse de que el backend esté usando la misma clave secreta
- Verificar que el token no haya expirado (24 horas)

### Problema: Errores CORS
**Solución:**
- Verificar configuración CORS en cada microservicio
- Asegurarse de que permitan el header `Authorization`
- Agregar `allowedHeaders` en configuración CORS del backend

### Problema: Usuario no redirigido al login
**Solución:**
- Verificar interceptores de axios en `tiendaService`
- Revisar console.log para ver errores 401
- Asegurarse de que `authUtils.clearAuth()` se esté llamando

---

**Fecha de implementación:** Diciembre 2025  
**Versión:** 1.0.0  
**Backend:** Spring Boot 3.x con JWT  
**Frontend:** React 18 + Vite
