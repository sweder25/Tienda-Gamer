# 🎯 JWT Frontend - Resumen de Implementación

## ✅ IMPLEMENTACIÓN COMPLETADA

Se ha implementado JWT completo en el frontend React de Tienda-Gamer.

---

## 📁 Archivos Creados (2)

1. **`/src/utils/authUtils.js`**
   - Manejo completo de tokens JWT
   - Funciones para localStorage
   - Validación de expiración
   - Headers de autorización

2. **`/src/utils/fetchWithAuth.js`**
   - Wrappers para fetch con JWT
   - Manejo automático de errores 401
   - Helpers: getWithAuth, postWithAuth, putWithAuth, deleteWithAuth

---

## 📝 Archivos Modificados (8)

1. **`/src/API/IngresoService.jsx`**
   - ✅ Endpoint: `/api/auth/login`
   - ✅ Endpoint: `/api/auth/register`
   - ✅ Endpoint: `/api/auth/validate`
   - ✅ Guarda token JWT en localStorage

2. **`/src/context/AuthContext.jsx`**
   - ✅ Método `register()` agregado
   - ✅ Validación de expiración de token
   - ✅ Auto-limpieza si token expiró

3. **`/src/API/productoService.jsx`**
   - ✅ Todas las peticiones incluyen token JWT

4. **`/src/API/ventaService.jsx`**
   - ✅ Todas las peticiones incluyen token JWT

5. **`/src/API/boletaService.jsx`**
   - ✅ Todas las peticiones incluyen token JWT

6. **`/src/API/registroService.jsx`**
   - ✅ Todas las peticiones incluyen token JWT

7. **`/src/API/tiendaService.jsx`**
   - ✅ Interceptor Axios para request (agrega token)
   - ✅ Interceptor Axios para response (maneja 401)

8. **`/src/pages/registro.jsx`**
   - ✅ Usa `useAuth()` en lugar de registroService
   - ✅ Auto-login después de registro exitoso

---

## 🔐 Flujo de Autenticación

### Login
```
Usuario → inicio.jsx → AuthContext.login() → IngresoService.login()
↓
Backend responde: { token: "...", usuario: {...} }
↓
localStorage.setItem('jwt_token', token)
localStorage.setItem('usuario', JSON.stringify(usuario))
↓
Redirect al Home
```

### Peticiones Protegidas
```
Componente → productoService.obtenerTodos()
↓
Headers: { Authorization: "Bearer <token>" }
↓
Backend valida token
↓
Si 401 → Redirect a /inicio
Si 200 → Datos retornados
```

### Logout
```
Usuario → Navbar → logout()
↓
localStorage.removeItem('jwt_token')
localStorage.removeItem('usuario')
↓
Redirect a /inicio
```

---

## 🚀 Cómo Usar

### En cualquier componente:

```jsx
import { useAuth } from '../context/AuthContext';

function MiComponente() {
  const { usuario, isAuthenticated, login, register, logout } = useAuth();
  
  // Verificar autenticación
  if (!isAuthenticated) {
    return <Navigate to="/inicio" />;
  }
  
  return <div>Hola {usuario.nombre}</div>;
}
```

### Hacer peticiones (automático):

```jsx
// Ya no necesitas hacer nada especial
import { productoService } from '../API/productoService';

const productos = await productoService.obtenerTodos();
// El token JWT se agrega automáticamente
```

---

## ⚙️ Configuración Necesaria en Backend

### 1. Servicio Ingreso (puerto 8080)
Debe tener estos endpoints:
- `POST /api/auth/login` → Retorna { token, usuario }
- `POST /api/auth/register` → Retorna { token, usuario }
- `GET /api/auth/validate` → Retorna 200 si token válido

### 2. Otros Microservicios (productos, ventas, boletas, etc.)
Deben:
- ✅ Tener JwtValidator
- ✅ Tener JwtInterceptor
- ✅ Usar la misma clave secreta (jwt.secret)
- ✅ Validar header: `Authorization: Bearer <token>`

---

## 🧪 Cómo Probar

### 1. Iniciar todos los microservicios del backend
```bash
# Asegúrate de que todos estén corriendo:
- IngresoApplication (8080)
- ProductosApplication (8083)
- VentasApplication (8087)
- BoletaApplication (8088)
- TiendaApplication (8085)
```

### 2. Iniciar el frontend
```bash
cd FullstackEva3Front/Tienda-Gamer
npm install  # Si es primera vez
npm run dev
```

### 3. Probar flujo de registro
1. Ir a http://localhost:5173/registro
2. Llenar formulario
3. Click en "Registrarse"
4. Verificar que redirige al home (si backend devuelve token)

### 4. Probar flujo de login
1. Ir a http://localhost:5173/inicio
2. Ingresar email y password
3. Click en "Iniciar Sesión"
4. Verificar que redirige al home

### 5. Verificar token en DevTools
1. Abrir DevTools (F12)
2. Ir a Application → Local Storage
3. Ver: `jwt_token` y `usuario`

### 6. Verificar headers en peticiones
1. Abrir DevTools → Network tab
2. Hacer cualquier acción (ver productos, crear venta, etc.)
3. Click en la petición
4. Ver Headers → Request Headers
5. Verificar: `Authorization: Bearer eyJ...`

### 7. Probar logout
1. Click en tu nombre en navbar
2. Click en "Cerrar Sesión"
3. Verificar que localStorage se limpió
4. Verificar que redirigió a /inicio

---

## ⚠️ Troubleshooting

### Problema: "Token inválido o expirado"
**Causa:** Token corrupto o expirado (24 horas)
**Solución:** 
- Hacer logout y login nuevamente
- Verificar en DevTools que el token es válido
- Verificar que backend use misma clave secreta

### Problema: Errores CORS
**Causa:** Backend no permite header Authorization
**Solución en cada microservicio:**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")  // ← Importante!
                .allowCredentials(true);
    }
}
```

### Problema: Usuario no redirigido a login al expirar token
**Causa:** Interceptores no configurados
**Solución:** 
- Verificar que tiendaService.jsx tenga los interceptores de axios
- Verificar que otros servicios usen authUtils.getAuthHeaders()

### Problema: 401 en todas las peticiones
**Causa:** Backend no está validando tokens o clave secreta diferente
**Solución:**
- Verificar que backend tenga JWT implementado (ver JWT_IMPLEMENTATION.md)
- Verificar que todos los servicios usen la misma clave secreta
- Verificar logs del backend para ver errores de validación

---

## 📚 Documentación Completa

Ver: **`JWT_FRONTEND_IMPLEMENTATION.md`** para documentación detallada

---

## 🎉 Resumen

✅ **Frontend 100% listo para JWT**
✅ **Todos los servicios incluyen token automáticamente**
✅ **Manejo de errores 401 automático**
✅ **Redirección a login si token expiró**
✅ **LocalStorage para persistencia**
✅ **AuthContext con login/register/logout**

**Próximo paso:** Asegurarse de que el backend tenga JWT implementado (ver JWT_IMPLEMENTATION.md en proyecto backend)
