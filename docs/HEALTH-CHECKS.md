# Verificación de Health Checks - MSBioSuper

## Resumen

Este documento describe la configuración y verificación de las sondas de liveness y readiness implementadas en MSBioSuper usando Spring Boot Actuator.

**Fecha de implementación**: 2026-04-23  
**Tarea**: 25.3 Configurar sondas de liveness y readiness  
**Requisito**: TR-16

---

## 1. Configuración Implementada

### 1.1 Propiedades de Actuator

Las siguientes propiedades han sido configuradas en `application.properties`:

```properties
# Habilitar endpoints de actuator
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Mostrar detalles de health check
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

# Habilitar health checks individuales
management.health.db.enabled=true
management.health.diskspace.enabled=true

# Configuración de sondas de liveness y readiness para Kubernetes
management.endpoint.health.probes.enabled=true
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true

# Configuración de grupos de health checks
management.endpoint.health.group.liveness.include=livenessState
management.endpoint.health.group.readiness.include=readinessState,db,diskSpace

# Base path para endpoints de actuator
management.endpoints.web.base-path=/actuator
```

### 1.2 Endpoints Disponibles

| Endpoint | Propósito | Uso |
|----------|-----------|-----|
| `/actuator/health` | Health check general | Monitoreo general de la aplicación |
| `/actuator/health/liveness` | Sonda de liveness | Kubernetes liveness probe |
| `/actuator/health/readiness` | Sonda de readiness | Kubernetes readiness probe |
| `/actuator/metrics` | Métricas de la aplicación | Monitoreo de rendimiento |
| `/actuator/prometheus` | Métricas en formato Prometheus | Integración con Prometheus |
| `/actuator/info` | Información de la aplicación | Versión, build info |

---

## 2. Sondas de Health

### 2.1 Liveness Probe

**Propósito**: Indica si la aplicación está viva y funcionando. Si falla, Kubernetes reiniciará el contenedor.

**Endpoint**: `GET /actuator/health/liveness`

**Componentes verificados**:
- `livenessState`: Estado de vida de la aplicación

**Estados posibles**:
- `UP`: La aplicación está viva
- `DOWN`: La aplicación debe ser reiniciada

**Respuesta esperada**:
```json
{
  "status": "UP",
  "components": {
    "livenessState": {
      "status": "UP"
    }
  }
}
```

### 2.2 Readiness Probe

**Propósito**: Indica si la aplicación está lista para recibir tráfico. Si falla, Kubernetes dejará de enviar tráfico al pod.

**Endpoint**: `GET /actuator/health/readiness`

**Componentes verificados**:
- `readinessState`: Estado de preparación de la aplicación
- `db`: Conectividad con PostgreSQL
- `diskSpace`: Espacio disponible en disco

**Estados posibles**:
- `UP`: La aplicación está lista para recibir tráfico
- `DOWN`: La aplicación no debe recibir tráfico
- `OUT_OF_SERVICE`: La aplicación está temporalmente fuera de servicio

**Respuesta esperada**:
```json
{
  "status": "UP",
  "components": {
    "readinessState": {
      "status": "UP"
    },
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 250000000000,
        "threshold": 10485760,
        "exists": true
      }
    }
  }
}
```

---

## 3. Verificación Manual

### 3.1 Prerequisitos

1. La aplicación debe estar corriendo
2. Las variables de entorno deben estar configuradas
3. La base de datos PostgreSQL debe estar accesible

### 3.2 Comandos de Verificación

#### Verificar Health General
```bash
curl -s http://localhost:8080/actuator/health | jq
```

**Respuesta esperada**: Status `UP` con todos los componentes activos

#### Verificar Liveness Probe
```bash
curl -s http://localhost:8080/actuator/health/liveness | jq
```

**Respuesta esperada**:
```json
{
  "status": "UP",
  "components": {
    "livenessState": {
      "status": "UP"
    }
  }
}
```

#### Verificar Readiness Probe
```bash
curl -s http://localhost:8080/actuator/health/readiness | jq
```

**Respuesta esperada**:
```json
{
  "status": "UP",
  "components": {
    "readinessState": {
      "status": "UP"
    },
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

#### Verificar Todos los Endpoints de Actuator
```bash
curl -s http://localhost:8080/actuator | jq
```

---

## 4. Integración con Kubernetes

### 4.1 Configuración de Deployment

Ejemplo de configuración de sondas en un Deployment de Kubernetes:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msbiosuper
spec:
  replicas: 3
  selector:
    matchLabels:
      app: msbiosuper
  template:
    metadata:
      labels:
        app: msbiosuper
    spec:
      containers:
      - name: msbiosuper
        image: msbiosuper:1.0.0
        ports:
        - containerPort: 8080
        
        # Liveness Probe - Reinicia el contenedor si falla
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60    # Esperar 60s después del inicio
          periodSeconds: 10           # Verificar cada 10s
          timeoutSeconds: 5           # Timeout de 5s
          failureThreshold: 3         # Reiniciar después de 3 fallos
          successThreshold: 1         # Considerar exitoso después de 1 éxito
        
        # Readiness Probe - Deja de enviar tráfico si falla
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30    # Esperar 30s después del inicio
          periodSeconds: 5           # Verificar cada 5s
          timeoutSeconds: 3          # Timeout de 3s
          failureThreshold: 3        # Marcar como no listo después de 3 fallos
          successThreshold: 1        # Marcar como listo después de 1 éxito
        
        # Startup Probe - Para aplicaciones con inicio lento
        startupProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 0
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30       # Permitir hasta 5 minutos de inicio (30 * 10s)
          successThreshold: 1
        
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: JDBC_SERVER
          valueFrom:
            secretKeyRef:
              name: msbiosuper-db-secret
              key: host
        - name: JDBC_PORT
          value: "5432"
        - name: JDBC_DATABASE
          valueFrom:
            secretKeyRef:
              name: msbiosuper-db-secret
              key: database
        - name: JDBC_USER
          valueFrom:
            secretKeyRef:
              name: msbiosuper-db-secret
              key: username
        - name: JDBC_PASSWORD
          valueFrom:
            secretKeyRef:
              name: msbiosuper-db-secret
              key: password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: msbiosuper-jwt-secret
              key: secret
        
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

### 4.2 Configuración Recomendada de Tiempos

| Probe | initialDelaySeconds | periodSeconds | timeoutSeconds | failureThreshold |
|-------|---------------------|---------------|----------------|------------------|
| **Liveness** | 60 | 10 | 5 | 3 |
| **Readiness** | 30 | 5 | 3 | 3 |
| **Startup** | 0 | 10 | 5 | 30 |

**Justificación**:
- **Liveness**: Espera más tiempo (60s) porque el reinicio es costoso
- **Readiness**: Responde más rápido (30s) para empezar a recibir tráfico pronto
- **Startup**: Permite hasta 5 minutos (30 * 10s) para aplicaciones con inicio lento

---

## 5. Escenarios de Prueba

### 5.1 Escenario 1: Aplicación Saludable

**Condición**: Aplicación iniciada correctamente, base de datos accesible

**Resultado esperado**:
- Liveness: `UP`
- Readiness: `UP`
- Kubernetes: Pod recibe tráfico

### 5.2 Escenario 2: Base de Datos No Disponible

**Condición**: PostgreSQL no accesible

**Resultado esperado**:
- Liveness: `UP` (la aplicación sigue viva)
- Readiness: `DOWN` (no lista para recibir tráfico)
- Kubernetes: Pod no recibe tráfico, pero no se reinicia

**Verificación**:
```bash
# Simular: Detener PostgreSQL
docker stop postgres-container

# Verificar readiness
curl -s http://localhost:8080/actuator/health/readiness | jq
# Esperado: status "DOWN", db.status "DOWN"

# Verificar liveness
curl -s http://localhost:8080/actuator/health/liveness | jq
# Esperado: status "UP" (la aplicación sigue viva)
```

### 5.3 Escenario 3: Espacio en Disco Bajo

**Condición**: Espacio en disco por debajo del umbral

**Resultado esperado**:
- Liveness: `UP`
- Readiness: `DOWN`
- Kubernetes: Pod no recibe tráfico

### 5.4 Escenario 4: Aplicación en Shutdown

**Condición**: Aplicación recibiendo señal de shutdown

**Resultado esperado**:
- Liveness: `DOWN`
- Readiness: `DOWN`
- Kubernetes: Pod se reinicia

---

## 6. Monitoreo y Alertas

### 6.1 Métricas Relacionadas

Las siguientes métricas de Prometheus están disponibles:

```promql
# Tiempo de respuesta del health check
http_server_requests_seconds{uri="/actuator/health/liveness"}
http_server_requests_seconds{uri="/actuator/health/readiness"}

# Estado de los componentes de health
health_component_status{component="db"}
health_component_status{component="diskSpace"}
```

### 6.2 Alertas Recomendadas

#### Alerta 1: Readiness Probe Fallando
```yaml
- alert: MSBioSuperNotReady
  expr: up{job="msbiosuper"} == 0
  for: 5m
  labels:
    severity: warning
  annotations:
    summary: "MSBioSuper no está listo para recibir tráfico"
    description: "El pod {{ $labels.pod }} ha estado en estado no listo por más de 5 minutos"
```

#### Alerta 2: Liveness Probe Fallando
```yaml
- alert: MSBioSuperDown
  expr: kube_pod_container_status_restarts_total{container="msbiosuper"} > 3
  for: 10m
  labels:
    severity: critical
  annotations:
    summary: "MSBioSuper se está reiniciando frecuentemente"
    description: "El pod {{ $labels.pod }} se ha reiniciado {{ $value }} veces en los últimos 10 minutos"
```

#### Alerta 3: Base de Datos No Disponible
```yaml
- alert: MSBioSuperDatabaseDown
  expr: health_component_status{component="db"} == 0
  for: 2m
  labels:
    severity: critical
  annotations:
    summary: "Base de datos PostgreSQL no disponible"
    description: "MSBioSuper no puede conectarse a PostgreSQL"
```

---

## 7. Troubleshooting

### 7.1 Problema: Liveness Probe Falla Constantemente

**Síntomas**:
- Pod se reinicia continuamente
- Logs muestran errores de inicio

**Posibles causas**:
1. `initialDelaySeconds` muy corto
2. Aplicación tarda mucho en iniciar
3. Recursos insuficientes (CPU/memoria)

**Solución**:
```yaml
# Aumentar initialDelaySeconds y usar startupProbe
livenessProbe:
  initialDelaySeconds: 120  # Aumentar a 2 minutos
  
startupProbe:
  failureThreshold: 60      # Permitir hasta 10 minutos
```

### 7.2 Problema: Readiness Probe Falla Intermitentemente

**Síntomas**:
- Pod entra y sale del estado ready
- Tráfico intermitente

**Posibles causas**:
1. Conexión a base de datos inestable
2. Timeouts muy cortos
3. Recursos insuficientes

**Solución**:
```yaml
readinessProbe:
  timeoutSeconds: 5         # Aumentar timeout
  failureThreshold: 5       # Aumentar tolerancia a fallos
```

### 7.3 Problema: Health Check Retorna 404

**Síntomas**:
- `curl /actuator/health` retorna 404

**Posibles causas**:
1. Actuator no habilitado
2. Endpoints no expuestos
3. Context path incorrecto

**Solución**:
```properties
# Verificar en application.properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoints.web.base-path=/actuator
```

### 7.4 Problema: Health Check Retorna 503

**Síntomas**:
- Health check retorna status 503 (Service Unavailable)

**Posibles causas**:
1. Base de datos no disponible
2. Espacio en disco insuficiente
3. Componente crítico fallando

**Solución**:
```bash
# Verificar detalles del health check
curl -s http://localhost:8080/actuator/health | jq

# Verificar logs
kubectl logs -f pod/msbiosuper-xxx

# Verificar conectividad a base de datos
kubectl exec -it pod/msbiosuper-xxx -- psql -h $JDBC_SERVER -U $JDBC_USER -d $JDBC_DATABASE
```

---

## 8. Checklist de Verificación

### 8.1 Configuración

- [x] Propiedades de Actuator configuradas en `application.properties`
- [x] Sondas de liveness y readiness habilitadas
- [x] Grupos de health checks configurados
- [x] Endpoints expuestos correctamente

### 8.2 Funcionalidad

- [ ] `/actuator/health` retorna status UP
- [ ] `/actuator/health/liveness` retorna status UP
- [ ] `/actuator/health/readiness` retorna status UP
- [ ] Health check de base de datos funciona
- [ ] Health check de espacio en disco funciona

### 8.3 Kubernetes (si aplica)

- [ ] Liveness probe configurado en Deployment
- [ ] Readiness probe configurado en Deployment
- [ ] Startup probe configurado (opcional)
- [ ] Tiempos de probe ajustados apropiadamente
- [ ] Recursos (CPU/memoria) configurados

### 8.4 Monitoreo

- [ ] Métricas de health checks disponibles en Prometheus
- [ ] Alertas configuradas para fallos de probes
- [ ] Dashboard de Grafana con health checks
- [ ] Logs estructurados para troubleshooting

---

## 9. Referencias

### 9.1 Documentación Oficial

- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Kubernetes Liveness, Readiness and Startup Probes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)
- [Spring Boot Health Indicators](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health)

### 9.2 Archivos Relacionados

- `src/main/resources/application.properties` - Configuración de Actuator
- `src/main/resources/application-prod.properties` - Configuración de producción
- `DEPLOYMENT.md` - Guía de despliegue con Kubernetes

---

## 10. Conclusión

Las sondas de liveness y readiness han sido configuradas exitosamente en MSBioSuper siguiendo las mejores prácticas de Spring Boot y Kubernetes.

**Estado**: ✅ COMPLETADO

**Próximos pasos**:
1. Verificar funcionamiento en entorno local
2. Probar en entorno de staging
3. Configurar en Kubernetes Deployment
4. Configurar alertas en Prometheus/Grafana
5. Documentar en runbook operacional

**Requisito cumplido**: TR-16 - Health Checks
