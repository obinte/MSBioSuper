#!/bin/bash

# ============================================================================
# Script de Prueba Simple - MSBioSuper
# ============================================================================

echo "🧪 PRUEBA SIMPLE DEL MICROSERVICIO MSBIOSUPER"
echo "=============================================="

BASE_URL="http://localhost:8082"

echo ""
echo "1. 🔍 Verificando que la aplicación responde..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/actuator/health" -o /dev/null)
echo "   Código de respuesta: $response"

if [[ "$response" == "401" ]]; then
    echo "   ✅ Aplicación funcionando (protegida por JWT)"
elif [[ "$response" == "200" ]]; then
    echo "   ✅ Aplicación funcionando (acceso público)"
else
    echo "   ❌ Problema con la aplicación"
fi

echo ""
echo "2. 📚 Verificando Swagger UI..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/swagger-ui.html" -o /dev/null)
echo "   Código de respuesta: $response"

if [[ "$response" == "200" || "$response" == "401" ]]; then
    echo "   ✅ Swagger UI disponible"
    echo "   🌐 Abre en tu navegador: $BASE_URL/swagger-ui.html"
else
    echo "   ❌ Problema con Swagger UI"
fi

echo ""
echo "3. 📖 Verificando OpenAPI docs..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/v3/api-docs" -o /dev/null)
echo "   Código de respuesta: $response"

if [[ "$response" == "200" || "$response" == "401" ]]; then
    echo "   ✅ OpenAPI docs disponibles"
    echo "   📋 URL para Postman: $BASE_URL/v3/api-docs"
else
    echo "   ❌ Problema con OpenAPI docs"
fi

echo ""
echo "4. 🔐 Probando endpoint de API sin autenticación..."
response=$(curl -s "$BASE_URL/api/v1/biosuperController/health" 2>/dev/null)
echo "   Respuesta: $response"

echo ""
echo "📋 RESUMEN DE ACCESO:"
echo "===================="
echo "🌐 Swagger UI (interfaz web): $BASE_URL/swagger-ui.html"
echo "📖 OpenAPI docs (para Postman): $BASE_URL/v3/api-docs"
echo "❤️  Health check: $BASE_URL/actuator/health"
echo "🔧 Debug remoto: puerto 5005"

echo ""
echo "🎯 RECOMENDACIONES:"
echo "==================="
echo "1. Abre Swagger UI en tu navegador para probar la API"
echo "2. O importa la especificación OpenAPI en Postman"
echo "3. Configura las credenciales JWT según tu implementación"
echo "4. Revisa los logs si hay problemas: docker-compose logs -f msbiosuper"

echo ""
echo "✅ VERIFICACIÓN COMPLETADA"