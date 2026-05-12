# Desarrollo con Docker - MSBioSuper

## Resumen

Este documento explica cómo usar Docker Compose para desarrollo local de MSBioSuper, incluyendo hot reload, debug remoto y configuraciones optimizadas para desarrollo.

## Archivos de Configuración

### docker-compose.yml
Configuración base para todos los entornos. Define:
- Servicio PostgreSQL
- Servicio MSBioSuper con configuración de producción
- Redes y volúmenes

### docker-compose.override.yml
Configuración específica para desarrollo que sobrescribe la configuración base. Incluye:
- **Hot reload** con Spring Boot DevTools
- **Debug remoto** en puerto 5005
- **Variables de entorno** optimizadas para desarrollo
- **Logging detallado** para debugging
- **LiveReload** para recarga automática del navegador

## Inicio Rápido

### 1. Preparar el Entorno

```bash
# Clonar el repositorio
git clone <repository-url>
cd msbiosuper

# Copiar variables de entorno
cp .env.example .env

# Editar variables de entorno (especialmente JWT_SECRET)
nano .env
```

### 2. Iniciar los Servicios

```bash
# Iniciar todos los servicios en background
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs solo de la aplicación
docker-compose logs -f msbiosuper
```

### 3. Verificar que Todo Funciona

```bash
# Verificar estado de los servicios
docker-compose ps

# Verificar health checks
curl http://localhost:8081/actuator/health

# Acceder a Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Características de Desarrollo

### 🔄 Hot Reload

El hot reload está configurado con Spring Boot DevTools:

**Configuración automática:**
- `SPRING_DEVTOOLS_RESTART_ENABLED=true`
- `SPRING_DEVTOOLS_LIVERELOAD_ENABLED=true`
- Puerto LiveReload: 35729

**Cómo usar:**
1. Hacer cambios en el código Java
2. Compilar en el IDE (Ctrl+F9 en IntelliJ)
3. La aplicación se reinicia automáticamente
4. El navegador se recarga automáticamente (con extensión LiveReload)

**Extensión LiveReload:**
- [Chrome](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei)
- [Firefox](https://addons.mozilla.org/en-US/firefox/addon/livereload-web-extension/)

### 🐛 Debug Remoto

Debug remoto configurado en puerto 5005 con JDWP:

**Configuración automática:**
- Puerto: 5005
- Protocolo: JDWP (Java Debug Wire Protocol)
- Modo: Attach (no suspend)

**Configurar en IDE:**

#### IntelliJ IDEA
1. Run → Edit Configurations
2. Add New Configuration → Remote JVM Debug
3. Host: `localhost`
4. Port: `5005`
5. Debugger mode: `Attach to remote JVM`
6. Use module classpath: `msbiosuper`

#### Visual Studio Code
Crear `.vscode/launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Debug MSBioSuper",
      "request": "attach",
      "hostName": "localhost",
      "port": 5005
    }
  ]
}
```

#### Eclipse
1. Run → Debug Configurations
2. Remote Java Application → New
3. Project: `msbiosuper`
4. Connection Type: `Standard (Socket Attach)`
5. Host: `localhost`
6. Port: `5005`

### 📊 Logging Detallado

Configuración de logging optimizada para desarrollo:

**Niveles configurados:**
- Aplicación: `DEBUG`
- Spring Framework: `INFO`
- Hibernate SQL: `DEBUG`
- Parámetros SQL: `TRACE`

**Ver logs específicos:**
```bash
# Logs de la aplicación
docker-compose logs -f msbiosuper | grep "com.acosux"

# Logs de SQL
docker-compose logs -f msbiosuper | grep "Hibernate:"

# Logs de Spring Security
docker-compose logs -f msbiosuper | grep "security"
```

### 🌐 Endpoints de Desarrollo

**Aplicación:**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Docs: http://localhost:8080/v3/api-docs

**Actuator (Monitoreo):**
- Health Check: http://localhost:8081/actuator/health
- Métricas: http://localhost:8081/actuator/metrics
- Environment: http://localhost:8081/actuator/env
- Loggers: http://localhost:8081/actuator/loggers
- Todos los endpoints: http://localhost:8081/actuator

**Base de Datos:**
- Host: localhost
- Puerto: 5432
- Base de datos: cipolliny_Abril_2026
- Usuario: administradores
- Contraseña: (ver archivo .env)

## Comandos Útiles

### Gestión de Servicios

```bash
# Iniciar servicios
docker-compose up -d

# Detener servicios
docker-compose stop

# Reiniciar servicios
docker-compose restart

# Detener y eliminar contenedores
docker-compose down

# Detener y eliminar contenedores + volúmenes
docker-compose down -v

# Reconstruir imágenes
docker-compose build --no-cache
docker-compose up -d --build
```

### Logs y Monitoreo

```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f msbiosuper
docker-compose logs -f postgres

# Ver últimas 100 líneas de logs
docker-compose logs --tail=100 msbiosuper

# Buscar en logs
docker-compose logs msbiosuper | grep "ERROR"
docker-compose logs msbiosuper | grep "SQL"
```

### Acceso a Contenedores

```bash
# Ejecutar comando en contenedor de la aplicación
docker-compose exec msbiosuper sh

# Ejecutar comando en contenedor de PostgreSQL
docker-compose exec postgres psql -U administradores -d cipolliny_Abril_2026

# Ver variables de entorno
docker-compose exec msbiosuper env

# Ver procesos en ejecución
docker-compose exec msbiosuper ps aux
```

### Base de Datos

```bash
# Conectar a PostgreSQL
docker-compose exec postgres psql -U administradores -d cipolliny_Abril_2026

# Hacer backup de la base de datos
docker-compose exec postgres pg_dump -U administradores cipolliny_Abril_2026 > backup.sql

# Restaurar backup
docker-compose exec -T postgres psql -U administradores cipolliny_Abril_2026 < backup.sql

# Ver tablas
docker-compose exec postgres psql -U administradores -d cipolliny_Abril_2026 -c "\dt"
```

## Configuración Avanzada

### Variables de Entorno Personalizadas

Puedes sobrescribir cualquier variable de entorno en el archivo `.env`:

```bash
# Ejemplo de .env personalizado
SPRING_PROFILES_ACTIVE=dev
LOGGING_LEVEL_COM_ACOSUX=TRACE
SPRING_JPA_SHOW_SQL=true
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,http://localhost:8080
```

### Volúmenes para Hot Reload

Para hot reload más eficiente, puedes montar el código fuente:

```yaml
# En docker-compose.override.yml
volumes:
  # Montar código fuente (requiere Maven en contenedor)
  - ./src:/app/src:ro
  - ./pom.xml:/app/pom.xml:ro
  - maven_cache:/root/.m2
```

### Configuración de Red

Los servicios están en la red `msbiosuper-network`:

```bash
# Ver configuración de red
docker network inspect msbiosuper-network

# Probar conectividad entre servicios
docker-compose exec msbiosuper ping postgres
```

## Troubleshooting

### Problemas Comunes

#### 1. La aplicación no inicia
```bash
# Verificar logs
docker-compose logs msbiosuper

# Verificar variables de entorno
docker-compose exec msbiosuper env | grep -E "(JDBC|JWT|SPRING)"

# Verificar conectividad a PostgreSQL
docker-compose exec msbiosuper ping postgres
```

#### 2. Debug remoto no conecta
```bash
# Verificar que el puerto está expuesto
docker-compose ps

# Verificar configuración JDWP
docker-compose logs msbiosuper | grep "Listening for transport"

# Verificar variables de entorno
docker-compose exec msbiosuper env | grep JAVA_TOOL_OPTIONS
```

#### 3. Hot reload no funciona
```bash
# Verificar DevTools
docker-compose exec msbiosuper env | grep DEVTOOLS

# Verificar volúmenes montados
docker-compose exec msbiosuper ls -la /app

# Recompilar manualmente
mvn compile
```

#### 4. Base de datos no conecta
```bash
# Verificar estado de PostgreSQL
docker-compose ps postgres

# Verificar logs de PostgreSQL
docker-compose logs postgres

# Probar conexión manual
docker-compose exec postgres psql -U administradores -d cipolliny_Abril_2026 -c "SELECT 1;"
```

### Limpiar el Entorno

```bash
# Detener y eliminar todo
docker-compose down -v

# Eliminar imágenes
docker rmi $(docker images -q msbiosuper*)

# Limpiar sistema Docker
docker system prune -a
```

## Mejores Prácticas

### 1. Gestión de Secretos
- Nunca commitear el archivo `.env`
- Usar secretos diferentes para cada entorno
- Rotar secretos JWT periódicamente

### 2. Desarrollo Eficiente
- Usar hot reload para cambios rápidos
- Usar debug remoto para troubleshooting
- Monitorear logs en tiempo real
- Usar Swagger UI para probar APIs

### 3. Base de Datos
- Hacer backups antes de cambios importantes
- Usar datos de prueba consistentes
- Limpiar datos de prueba regularmente

### 4. Rendimiento
- Asignar recursos suficientes a Docker
- Usar volúmenes para datos persistentes
- Monitorear uso de memoria y CPU

## Referencias

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)
- [Java Debug Wire Protocol](https://docs.oracle.com/javase/8/docs/technotes/guides/jpda/jdwp-spec.html)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

---

**Documento creado**: 2026-04-24  
**Autor**: Josias Rubén Aldaz Arcalles  
**Versión**: 1.0