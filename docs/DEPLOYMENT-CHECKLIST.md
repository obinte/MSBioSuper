# Checklist de Despliegue - MSBioSuper Java 21

## Información del Documento
- **Proyecto**: MSBioSuper Java 21 Migration
- **Versión**: 1.0
- **Fecha de Creación**: 2026-05-05
- **Autor**: Equipo de Desarrollo MSBioSuper
- **Estado**: ACTIVO

---

## 📋 Resumen del Checklist

Este checklist garantiza un despliegue seguro y exitoso de MSBioSuper en producción, incluyendo procedimientos de respaldo, verificación y rollback.

### Fases del Despliegue
1. **Pre-Despliegue**: Preparación y respaldos
2. **Despliegue**: Ejecución del despliegue
3. **Post-Despliegue**: Verificación y monitoreo
4. **Rollback**: Procedimiento de emergencia (si es necesario)

---

## 🔄 FASE 1: PRE-DESPLIEGUE

### 1.1 Preparación del Entorno

#### ✅ Verificación de Prerequisitos

- [ ] **Java 21 Runtime**: Verificar que Java 21 está instalado en el servidor de producción
  ```bash
  java -version
  # Debe mostrar: openjdk version "21.x.x"
  ```

- [ ] **PostgreSQL**: Verificar conectividad a la base de datos de producción
  ```bash
  psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE -c "SELECT version();"
  ```

- [ ] **Docker**: Verificar que Docker está disponible (si se usa contenedorización)
  ```bash
  docker --version
  docker-compose --version
  ```

- [ ] **Espacio en Disco**: Verificar espacio suficiente (mínimo 2GB libres)
  ```bash
  df -h
  ```

- [ ] **Memoria RAM**: Verificar memoria disponible (mínimo 1GB libre)
  ```bash
  free -h
  ```

#### ✅ Configuración de Variables de Entorno

- [ ] **Variables Obligatorias Configuradas**:
  ```bash
  # Verificar que todas las variables están configuradas
  echo "JDBC_DATABASE: $JDBC_DATABASE"
  echo "JDBC_USER: $JDBC_USER"
  echo "JDBC_PASSWORD: [CONFIGURADO: $([ -n "$JDBC_PASSWORD" ] && echo "SÍ" || echo "NO")]"
  echo "JWT_SECRET: [CONFIGURADO: $([ -n "$JWT_SECRET" ] && echo "SÍ" || echo "NO")]"
  ```

- [ ] **Variables Opcionales Revisadas**:
  ```bash
  echo "JDBC_SERVER: ${JDBC_SERVER:-localhost}"
  echo "JDBC_PORT: ${JDBC_PORT:-5432}"
  echo "SERVER_PORT: ${SERVER_PORT:-8080}"
  echo "SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-prod}"
  ```

- [ ] **Validación de JWT Secret**: Verificar que tiene al menos 256 bits
  ```bash
  echo -n "$JWT_SECRET" | base64 -d | wc -c
  # Debe ser >= 32 bytes
  ```

#### ✅ Preparación de Archivos

- [ ] **Imagen Docker Construida** (si aplica):
  ```bash
  docker images | grep msbiosuper
  ```

- [ ] **JAR de Aplicación Disponible**:
  ```bash
  ls -la target/msbiosuper-*.jar
  ```

- [ ] **Archivos de Configuración**:
  - [ ] `application-prod.properties` actualizado
  - [ ] `docker-compose.yml` configurado para producción
  - [ ] Scripts de despliegue disponibles

### 1.2 Respaldo de Base de Datos

#### ✅ Respaldo Completo de PostgreSQL

**⚠️ CRÍTICO**: Este paso es OBLIGATORIO antes de cualquier despliegue.

- [ ] **Crear directorio de respaldos**:
  ```bash
  BACKUP_DIR="/backups/msbiosuper/$(date +%Y%m%d_%H%M%S)"
  mkdir -p $BACKUP_DIR
  ```

- [ ] **Respaldo completo de la base de datos**:
  ```bash
  pg_dump -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE \
    --verbose --clean --create --if-exists \
    --file="$BACKUP_DIR/msbiosuper_full_backup.sql"
  ```

- [ ] **Respaldo solo de datos (sin esquema)**:
  ```bash
  pg_dump -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE \
    --data-only --verbose \
    --file="$BACKUP_DIR/msbiosuper_data_only.sql"
  ```

- [ ] **Respaldo solo de esquema (sin datos)**:
  ```bash
  pg_dump -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE \
    --schema-only --verbose \
    --file="$BACKUP_DIR/msbiosuper_schema_only.sql"
  ```

- [ ] **Verificar integridad de respaldos**:
  ```bash
  # Verificar que los archivos no están vacíos
  ls -lh $BACKUP_DIR/
  
  # Verificar que contienen datos válidos
  head -20 $BACKUP_DIR/msbiosuper_full_backup.sql
  ```

- [ ] **Comprimir respaldos**:
  ```bash
  cd $BACKUP_DIR
  gzip *.sql
  ```

- [ ] **Documentar información del respaldo**:
  ```bash
  cat > $BACKUP_DIR/backup_info.txt << EOF
  Fecha de respaldo: $(date)
  Base de datos: $JDBC_DATABASE
  Servidor: $JDBC_SERVER:$JDBC_PORT
  Usuario: $JDBC_USER
  Versión de PostgreSQL: $(psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE -t -c "SELECT version();")
  Tamaño de la base de datos: $(psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE -t -c "SELECT pg_size_pretty(pg_database_size('$JDBC_DATABASE'));")
  EOF
  ```

#### ✅ Respaldo de Configuración Actual

- [ ] **Respaldo de aplicación actual** (si existe):
  ```bash
  CURRENT_APP_BACKUP="$BACKUP_DIR/current_application"
  mkdir -p $CURRENT_APP_BACKUP
  
  # Respaldar JAR actual
  cp /opt/msbiosuper/msbiosuper.jar $CURRENT_APP_BACKUP/ 2>/dev/null || echo "No hay aplicación previa"
  
  # Respaldar configuración actual
  cp /opt/msbiosuper/application.properties $CURRENT_APP_BACKUP/ 2>/dev/null || echo "No hay configuración previa"
  
  # Respaldar logs actuales
  cp -r /opt/msbiosuper/logs $CURRENT_APP_BACKUP/ 2>/dev/null || echo "No hay logs previos"
  ```

### 1.3 Notificación de Mantenimiento

- [ ] **Notificar a usuarios** sobre ventana de mantenimiento
- [ ] **Coordinar con equipo de soporte** sobre el despliegue
- [ ] **Verificar que no hay operaciones críticas** en curso

---

## 🚀 FASE 2: DESPLIEGUE

### 2.1 Detener Servicios Actuales

#### ✅ Parada Controlada de la Aplicación

- [ ] **Verificar servicios en ejecución**:
  ```bash
  # Si usa systemd
  systemctl status msbiosuper
  
  # Si usa Docker
  docker ps | grep msbiosuper
  
  # Si usa proceso directo
  ps aux | grep msbiosuper
  ```

- [ ] **Parada graceful de la aplicación**:
  ```bash
  # Opción 1: systemd
  sudo systemctl stop msbiosuper
  
  # Opción 2: Docker
  docker-compose down
  
  # Opción 3: Proceso directo (enviar SIGTERM)
  pkill -TERM -f msbiosuper
  ```

- [ ] **Verificar que la aplicación se detuvo completamente**:
  ```bash
  # Verificar que no hay procesos Java corriendo
  ps aux | grep java | grep msbiosuper
  
  # Verificar que el puerto está libre
  netstat -tlnp | grep :8080
  ```

- [ ] **Esperar tiempo de gracia** (30 segundos) para conexiones activas

### 2.2 Despliegue de Nueva Versión

#### ✅ Opción A: Despliegue con JAR

- [ ] **Crear directorio de aplicación**:
  ```bash
  sudo mkdir -p /opt/msbiosuper
  sudo chown $USER:$USER /opt/msbiosuper
  ```

- [ ] **Copiar nuevo JAR**:
  ```bash
  cp target/msbiosuper-*.jar /opt/msbiosuper/msbiosuper.jar
  ```

- [ ] **Configurar permisos**:
  ```bash
  chmod +x /opt/msbiosuper/msbiosuper.jar
  ```

- [ ] **Crear script de inicio**:
  ```bash
  cat > /opt/msbiosuper/start.sh << 'EOF'
  #!/bin/bash
  cd /opt/msbiosuper
  
  # Cargar variables de entorno
  source /opt/msbiosuper/.env
  
  # Iniciar aplicación
  java -jar msbiosuper.jar \
    --spring.profiles.active=prod \
    --server.port=${SERVER_PORT:-8080} \
    > logs/msbiosuper.log 2>&1 &
  
  echo $! > msbiosuper.pid
  EOF
  
  chmod +x /opt/msbiosuper/start.sh
  ```

#### ✅ Opción B: Despliegue con Docker

- [ ] **Cargar nueva imagen Docker**:
  ```bash
  # Si la imagen está en un archivo
  docker load < msbiosuper-latest.tar.gz
  
  # Si se construye localmente
  docker build -t msbiosuper:latest .
  ```

- [ ] **Actualizar docker-compose.yml**:
  ```bash
  cp docker-compose.yml /opt/msbiosuper/
  cp docker-compose.prod.yml /opt/msbiosuper/docker-compose.override.yml
  ```

- [ ] **Configurar variables de entorno**:
  ```bash
  cat > /opt/msbiosuper/.env << EOF
  SPRING_PROFILES_ACTIVE=prod
  JDBC_SERVER=$JDBC_SERVER
  JDBC_PORT=$JDBC_PORT
  JDBC_DATABASE=$JDBC_DATABASE
  JDBC_USER=$JDBC_USER
  JDBC_PASSWORD=$JDBC_PASSWORD
  JWT_SECRET=$JWT_SECRET
  SERVER_PORT=${SERVER_PORT:-8080}
  EOF
  ```

### 2.3 Iniciar Nueva Versión

#### ✅ Inicio de la Aplicación

- [ ] **Crear directorio de logs**:
  ```bash
  mkdir -p /opt/msbiosuper/logs
  ```

- [ ] **Iniciar aplicación**:
  ```bash
  # Opción A: JAR directo
  cd /opt/msbiosuper
  ./start.sh
  
  # Opción B: Docker Compose
  cd /opt/msbiosuper
  docker-compose up -d
  ```

- [ ] **Verificar que el proceso inició**:
  ```bash
  # Opción A: Verificar PID
  cat /opt/msbiosuper/msbiosuper.pid
  ps -p $(cat /opt/msbiosuper/msbiosuper.pid)
  
  # Opción B: Verificar contenedor
  docker-compose ps
  ```

- [ ] **Monitorear logs de inicio**:
  ```bash
  # Opción A: Logs de archivo
  tail -f /opt/msbiosuper/logs/msbiosuper.log
  
  # Opción B: Logs de Docker
  docker-compose logs -f msbiosuper
  ```

- [ ] **Esperar a que la aplicación esté lista** (típicamente 30-60 segundos)

---

## ✅ FASE 3: POST-DESPLIEGUE (VERIFICACIÓN)

### 3.1 Verificaciones Técnicas

#### ✅ Health Checks Básicos

- [ ] **Verificar que la aplicación responde**:
  ```bash
  # Health check básico
  curl -f http://localhost:8080/actuator/health
  
  # Debe retornar: {"status":"UP"}
  ```

- [ ] **Verificar health check detallado**:
  ```bash
  curl -s http://localhost:8080/actuator/health | jq
  
  # Verificar que todos los componentes están UP:
  # - db: UP
  # - diskSpace: UP
  # - ping: UP
  ```

- [ ] **Verificar conectividad a base de datos**:
  ```bash
  curl -s http://localhost:8080/actuator/health | jq '.components.db.status'
  # Debe retornar: "UP"
  ```

- [ ] **Verificar métricas**:
  ```bash
  curl -f http://localhost:8080/actuator/prometheus | head -20
  ```

#### ✅ Verificaciones de Configuración

- [ ] **Verificar perfil activo**:
  ```bash
  curl -s http://localhost:8080/actuator/info | jq '.activeProfiles'
  # Debe mostrar: ["prod"]
  ```

- [ ] **Verificar versión de la aplicación**:
  ```bash
  curl -s http://localhost:8080/actuator/info | jq '.build'
  ```

- [ ] **Verificar configuración de base de datos**:
  ```bash
  # Verificar en logs que HikariCP se configuró correctamente
  grep -i "hikari" /opt/msbiosuper/logs/msbiosuper.log | tail -5
  ```

### 3.2 Verificaciones Funcionales

#### ✅ Autenticación JWT

- [ ] **Probar endpoint de autenticación**:
  ```bash
  # Obtener token JWT (ajustar credenciales según sea necesario)
  TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"test_user","password":"test_password"}' | jq -r '.token')
  
  echo "Token obtenido: ${TOKEN:0:50}..."
  ```

- [ ] **Verificar que el token es válido**:
  ```bash
  curl -f -H "Authorization: Bearer $TOKEN" \
    http://localhost:8080/actuator/health
  ```

#### ✅ Endpoints de API

- [ ] **Probar endpoint de resumen de pesca**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/biosuperController/getListaResumenPesca \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"fechaInicio":"2024-01-01","fechaFin":"2024-01-31","empresa":"TEST"}' \
    -w "HTTP Status: %{http_code}\n"
  ```

- [ ] **Probar endpoint de productos**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/biosuperController/getListaProductosGeneralTO \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"empresa":"TEST"}' \
    -w "HTTP Status: %{http_code}\n"
  ```

- [ ] **Verificar manejo de errores**:
  ```bash
  # Probar con token inválido
  curl -X POST http://localhost:8080/api/v1/biosuperController/getListaResumenPesca \
    -H "Authorization: Bearer invalid_token" \
    -H "Content-Type: application/json" \
    -d '{}' \
    -w "HTTP Status: %{http_code}\n"
  # Debe retornar 401 Unauthorized
  ```

#### ✅ Documentación de API

- [ ] **Verificar OpenAPI docs**:
  ```bash
  curl -f http://localhost:8080/v3/api-docs | jq '.info.title'
  # Debe retornar: "MSBioSuper API"
  ```

- [ ] **Verificar Swagger UI** (si está habilitado):
  ```bash
  curl -f http://localhost:8080/swagger-ui.html | grep -i "swagger"
  ```

### 3.3 Verificaciones de Rendimiento

#### ✅ Métricas de Rendimiento

- [ ] **Verificar tiempo de respuesta**:
  ```bash
  # Medir tiempo de respuesta del health check
  time curl -s http://localhost:8080/actuator/health > /dev/null
  # Debe ser < 1 segundo
  ```

- [ ] **Verificar uso de memoria**:
  ```bash
  # Opción A: Desde métricas de la aplicación
  curl -s http://localhost:8080/actuator/prometheus | grep jvm_memory_used_bytes
  
  # Opción B: Desde sistema operativo
  ps aux | grep java | grep msbiosuper
  ```

- [ ] **Verificar conexiones de base de datos**:
  ```bash
  curl -s http://localhost:8080/actuator/prometheus | grep hikaricp_connections
  ```

#### ✅ Pruebas de Carga Básicas

- [ ] **Prueba de múltiples requests concurrentes**:
  ```bash
  # Ejecutar 10 requests concurrentes
  for i in {1..10}; do
    curl -s http://localhost:8080/actuator/health &
  done
  wait
  echo "Todas las requests completadas"
  ```

### 3.4 Verificaciones de Seguridad

#### ✅ Configuración de Seguridad

- [ ] **Verificar CORS**:
  ```bash
  curl -H "Origin: https://unauthorized-domain.com" \
    -H "Access-Control-Request-Method: POST" \
    -H "Access-Control-Request-Headers: X-Requested-With" \
    -X OPTIONS \
    http://localhost:8080/api/v1/biosuperController/getListaResumenPesca \
    -w "HTTP Status: %{http_code}\n"
  # Debe rechazar orígenes no autorizados
  ```

- [ ] **Verificar que endpoints sensibles requieren autenticación**:
  ```bash
  curl http://localhost:8080/api/v1/biosuperController/getListaResumenPesca \
    -w "HTTP Status: %{http_code}\n"
  # Debe retornar 401 Unauthorized
  ```

- [ ] **Verificar que información sensible no se expone**:
  ```bash
  # Verificar que /actuator/env no está expuesto
  curl http://localhost:8080/actuator/env -w "HTTP Status: %{http_code}\n"
  # Debe retornar 404 Not Found
  ```

### 3.5 Monitoreo Continuo

#### ✅ Configurar Monitoreo

- [ ] **Verificar que las métricas se están recolectando**:
  ```bash
  # Verificar que Prometheus puede scrape las métricas
  curl -s http://localhost:8080/actuator/prometheus | wc -l
  # Debe retornar > 100 líneas de métricas
  ```

- [ ] **Configurar alertas básicas** (si aplica):
  - [ ] Alerta si la aplicación está down
  - [ ] Alerta si el uso de memoria > 80%
  - [ ] Alerta si las conexiones de DB > 15

- [ ] **Verificar logs estructurados**:
  ```bash
  tail -5 /opt/msbiosuper/logs/msbiosuper.log | jq
  # Los logs deben estar en formato JSON válido
  ```

---

## 🔄 FASE 4: ROLLBACK (PROCEDIMIENTO DE EMERGENCIA)

### 4.1 Criterios para Rollback

**Ejecutar rollback inmediatamente si**:
- [ ] La aplicación no inicia después de 5 minutos
- [ ] Health checks fallan consistentemente
- [ ] Errores críticos en > 50% de las requests
- [ ] Pérdida de conectividad a base de datos
- [ ] Uso de memoria > 90% sostenido
- [ ] Cualquier error que afecte la funcionalidad crítica

### 4.2 Procedimiento de Rollback

#### ✅ Paso 1: Detener Nueva Versión

- [ ] **Detener aplicación actual**:
  ```bash
  # Opción A: JAR directo
  if [ -f /opt/msbiosuper/msbiosuper.pid ]; then
    kill $(cat /opt/msbiosuper/msbiosuper.pid)
  fi
  
  # Opción B: Docker
  docker-compose down
  
  # Verificar que se detuvo
  ps aux | grep java | grep msbiosuper
  ```

#### ✅ Paso 2: Restaurar Versión Anterior

- [ ] **Restaurar aplicación anterior**:
  ```bash
  # Encontrar el backup más reciente
  LATEST_BACKUP=$(ls -t /backups/msbiosuper/*/current_application/msbiosuper.jar 2>/dev/null | head -1)
  
  if [ -n "$LATEST_BACKUP" ]; then
    echo "Restaurando desde: $LATEST_BACKUP"
    cp "$LATEST_BACKUP" /opt/msbiosuper/msbiosuper.jar
  else
    echo "ERROR: No se encontró backup de aplicación anterior"
    exit 1
  fi
  ```

- [ ] **Restaurar configuración anterior**:
  ```bash
  LATEST_CONFIG=$(ls -t /backups/msbiosuper/*/current_application/application.properties 2>/dev/null | head -1)
  
  if [ -n "$LATEST_CONFIG" ]; then
    cp "$LATEST_CONFIG" /opt/msbiosuper/application.properties
  fi
  ```

#### ✅ Paso 3: Restaurar Base de Datos (Solo si es Necesario)

**⚠️ ADVERTENCIA**: Solo ejecutar si hay corrupción de datos confirmada.

- [ ] **Evaluar si es necesario restaurar la base de datos**:
  ```bash
  # Probar conectividad básica
  psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE -c "SELECT COUNT(*) FROM information_schema.tables;"
  ```

- [ ] **Si es necesario, restaurar desde backup**:
  ```bash
  # Encontrar el backup más reciente
  LATEST_DB_BACKUP=$(ls -t /backups/msbiosuper/*/msbiosuper_full_backup.sql.gz | head -1)
  
  if [ -n "$LATEST_DB_BACKUP" ]; then
    echo "⚠️  RESTAURANDO BASE DE DATOS desde: $LATEST_DB_BACKUP"
    echo "Esto ELIMINARÁ todos los datos actuales. ¿Continuar? (yes/no)"
    read -r CONFIRM
    
    if [ "$CONFIRM" = "yes" ]; then
      # Descomprimir y restaurar
      gunzip -c "$LATEST_DB_BACKUP" | psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE
    else
      echo "Restauración de BD cancelada"
    fi
  fi
  ```

#### ✅ Paso 4: Reiniciar Versión Anterior

- [ ] **Iniciar aplicación anterior**:
  ```bash
  cd /opt/msbiosuper
  ./start.sh
  ```

- [ ] **Verificar que la versión anterior funciona**:
  ```bash
  # Esperar 60 segundos para el inicio
  sleep 60
  
  # Verificar health check
  curl -f http://localhost:8080/actuator/health
  ```

- [ ] **Probar funcionalidad crítica**:
  ```bash
  # Probar autenticación y un endpoint básico
  # (usar los mismos comandos de la sección de verificación)
  ```

### 4.3 Post-Rollback

#### ✅ Acciones Después del Rollback

- [ ] **Documentar el incidente**:
  ```bash
  cat > /tmp/rollback_report_$(date +%Y%m%d_%H%M%S).txt << EOF
  REPORTE DE ROLLBACK - MSBioSuper
  
  Fecha y hora: $(date)
  Razón del rollback: [DESCRIBIR AQUÍ]
  Versión revertida: [VERSIÓN NUEVA]
  Versión restaurada: [VERSIÓN ANTERIOR]
  
  Logs relevantes:
  $(tail -50 /opt/msbiosuper/logs/msbiosuper.log)
  
  Estado actual:
  $(curl -s http://localhost:8080/actuator/health)
  EOF
  ```

- [ ] **Notificar al equipo** sobre el rollback exitoso

- [ ] **Investigar la causa raíz** del problema

- [ ] **Planificar corrección** para el próximo intento de despliegue

### 4.4 Verificación Post-Rollback

- [ ] **Ejecutar todas las verificaciones** de la Fase 3 (Post-Despliegue)
- [ ] **Confirmar que el sistema está estable** durante al menos 30 minutos
- [ ] **Verificar que no hay pérdida de datos** comparando con el backup

---

## 📊 MÉTRICAS Y TIEMPOS ESPERADOS

### Tiempos de Despliegue

| Fase | Tiempo Estimado | Tiempo Máximo |
|------|-----------------|---------------|
| Pre-Despliegue | 15-30 minutos | 45 minutos |
| Respaldo de BD | 5-15 minutos | 30 minutos |
| Despliegue | 5-10 minutos | 15 minutos |
| Verificación | 15-30 minutos | 45 minutos |
| **TOTAL** | **40-85 minutos** | **2.25 horas** |

### Tiempos de Rollback

| Acción | Tiempo Estimado |
|--------|-----------------|
| Detener aplicación | 1-2 minutos |
| Restaurar aplicación | 2-5 minutos |
| Restaurar BD (si necesario) | 10-30 minutos |
| Verificación | 10-15 minutos |
| **TOTAL** | **23-52 minutos** |

### Criterios de Éxito

- [ ] **Tiempo de inicio** < 2 minutos
- [ ] **Health check** responde en < 1 segundo
- [ ] **Autenticación JWT** funciona correctamente
- [ ] **Todos los endpoints** responden con códigos 2xx o 4xx apropiados
- [ ] **Uso de memoria** < 512MB después de 10 minutos
- [ ] **Conexiones de BD** < 10 activas en estado idle
- [ ] **Sin errores** en logs durante los primeros 15 minutos

---

## 🚨 CONTACTOS DE EMERGENCIA

### Equipo de Desarrollo
- **Desarrollador Principal**: [Nombre] - [Teléfono] - [Email]
- **DevOps**: [Nombre] - [Teléfono] - [Email]

### Equipo de Infraestructura
- **DBA**: [Nombre] - [Teléfono] - [Email]
- **Administrador de Sistemas**: [Nombre] - [Teléfono] - [Email]

### Escalación
- **Gerente de Proyecto**: [Nombre] - [Teléfono] - [Email]
- **CTO**: [Nombre] - [Teléfono] - [Email]

---

## 📝 REGISTRO DE DESPLIEGUES

### Plantilla de Registro

```
DESPLIEGUE MSBioSuper - [FECHA]

Versión desplegada: [VERSION]
Ejecutado por: [NOMBRE]
Inicio: [HORA_INICIO]
Fin: [HORA_FIN]
Duración total: [DURACION]

Checklist completado: [SÍ/NO]
Rollback necesario: [SÍ/NO]
Incidencias: [DESCRIPCIÓN]

Verificaciones:
- Health checks: [OK/FAIL]
- Autenticación: [OK/FAIL]
- Endpoints API: [OK/FAIL]
- Rendimiento: [OK/FAIL]

Notas adicionales:
[COMENTARIOS]
```

---

## 🔗 REFERENCIAS

- **Configuración de Producción**: [PRODUCTION-CONFIGURATION.md](./PRODUCTION-CONFIGURATION.md)
- **Variables de Entorno**: [ENVIRONMENT-VARIABLES.md](./ENVIRONMENT-VARIABLES.md)
- **Configuración JWT**: [JWT-CONFIGURATION.md](./JWT-CONFIGURATION.md)
- **Docker**: [DOCKER.md](./DOCKER.md)
- **Health Checks**: [HEALTH-CHECKS.md](./HEALTH-CHECKS.md)

---

**Última actualización**: 2026-05-05  
**Versión del checklist**: 1.0  
**Próxima revisión**: 2026-08-05